# Projeto - Cidades ESG Inteligentes

## InovaGAB Backend — DevOps

Projeto acadêmico da FIAP preparado para a fase **Navegando pelo mundo DevOps**. A aplicação base é o backend InovaGAB, desenvolvido em Java/Spring Boot para gestão de estratégias, ideias e projetos. Nesta entrega, o foco é aplicar práticas de DevOps ao ciclo de vida da aplicação, incluindo **CI/CD, containerização, orquestração, staging e produção**.

A aplicação pode ser usada para organizar iniciativas ESG, como projetos de eficiência operacional, redução de custos, produtividade e inovação sustentável.

### Integrantes

- Caio Ândres Salvador Riva — RM 564661
- Jessica Ghirardelli Tinguely — RM 565574
- Iara Venâncio Ribeiro — RM 563242

---

## Como executar localmente com Docker

### Pré-requisitos

- Docker
- Docker Compose

### 1. Criar o arquivo de variáveis de ambiente

Na raiz do projeto:

```bash
cp .env.example .env
```

Edite o `.env` e troque, no mínimo, o `JWT_SECRET`.

Exemplo:

```env
MONGODB_URI=mongodb://mongodb:27017
MONGODB_DATABASE=inovagab
JWT_SECRET=uma-chave-segura-com-mais-de-32-caracteres
GEMINI_API_KEY=
GEMINI_MODEL=gemini-2.5-flash
PORT=8080
```

> O arquivo `.env` real não deve ser enviado para o GitHub. O repositório contém apenas `.env.example`.

### 2. Subir aplicação e banco

```bash
docker compose up --build -d
```

O Docker Compose cria:

- API Spring Boot na porta `8080`;
- MongoDB 7;
- volume persistente `inovagab-mongo-data`;
- rede Docker `inovagab-network`.

### 3. Conferir os containers

```bash
docker compose ps
```

### 4. Testar o health check

```bash
curl http://localhost:8080/health
```

Resposta esperada:

```json
{
  "status": "UP",
  "application": "inovagab-backend",
  "timestamp": "..."
}
```

### 5. Testar login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"gestor@inovagab.com","senha":"123456"}'
```

Também existe um script de smoke test:

```bash
./scripts/smoke-test.sh http://localhost:8080
```

### 6. Parar o ambiente

```bash
docker compose down
```

Para remover também o volume do MongoDB:

```bash
docker compose down -v
```

---

## Pipeline CI/CD

A automação está em:

```text
.github/workflows/ci-cd.yml
```

Foi utilizado **GitHub Actions**.

### Fluxo

```text
Push / Pull Request
        │
        ▼
Checkout do código
        │
        ▼
Configuração Java 21
        │
        ▼
MongoDB de teste
        │
        ▼
Maven Test
        │
        ▼
Maven Package
        │
        ▼
Docker Build
        │
        ├──────── develop ────────► Deploy Staging ─► Health Check
        │
        └──────── main ───────────► Deploy Produção ─► Health Check
```

### Etapas automatizadas

1. **Build**
   - Java 21 com Temurin.
   - Maven Wrapper.
   - Geração do artefato `.jar`.

2. **Testes**
   - MongoDB 7 executado como service container no GitHub Actions.
   - Execução de `./mvnw -B clean test`.

3. **Validação do Docker**
   - O pipeline executa `docker build` para garantir que a imagem seja gerada corretamente.

4. **Staging**
   - Push na branch `develop`.
   - O GitHub Actions chama o Deploy Hook do Render.
   - Após o deploy, o pipeline consulta `/health`.

5. **Produção**
   - Push na branch `main`.
   - O GitHub Actions chama o Deploy Hook de produção.
   - Após o deploy, o pipeline consulta `/health`.

### Secrets utilizados pelo pipeline

No environment `staging`:

```text
RENDER_DEPLOY_HOOK_STAGING
STAGING_URL
```

No environment `production`:

```text
RENDER_DEPLOY_HOOK_PRODUCTION
PRODUCTION_URL
```

As instruções completas estão em [`docs/DEPLOY.md`](docs/DEPLOY.md).

---

## Containerização

Foi adotado um **Dockerfile multi-stage** para separar a compilação da execução da aplicação.

### Dockerfile

```dockerfile
# Stage 1: build da aplicação
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn -B -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests \
    && JAR_FILE=$(find target -maxdepth 1 -type f -name '*.jar' ! -name '*.original' | head -n 1) \
    && test -n "$JAR_FILE" \
    && cp "$JAR_FILE" /tmp/app.jar

# Stage 2: imagem de execução
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN groupadd --system appgroup && useradd --system --gid appgroup appuser
COPY --from=build /tmp/app.jar app.jar

USER appuser
EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "/app/app.jar"]
```

### Estratégias adotadas

- **Multi-stage build:** Maven fica somente na etapa de compilação.
- **Imagem runtime menor:** produção utiliza somente Java JRE 21.
- **Usuário sem privilégios:** a API não é executada como `root`.
- **Configuração externa:** credenciais e URLs são fornecidas por variáveis de ambiente.
- **Persistência:** MongoDB utiliza volume Docker.
- **Isolamento:** API e banco compartilham uma rede específica.
- **Dependência saudável:** a API aguarda o health check do MongoDB no Compose.

---

## Orquestração com Docker Compose

O arquivo `docker-compose.yml` sobe os dois serviços necessários:

```text
┌─────────────────────────┐
│        API Java         │
│ Spring Boot · porta 8080│
└───────────┬─────────────┘
            │ inovagab-network
            ▼
┌─────────────────────────┐
│        MongoDB 7        │
│  Volume persistente     │
└─────────────────────────┘
```

O Compose utiliza:

- `volumes`;
- `networks`;
- `environment`;
- `.env`;
- `healthcheck`;
- `depends_on`.

---

## Ambientes

### Staging

Branch responsável:

```text
develop
```

Banco recomendado:

```text
inovagab_staging
```

URL após configuração no Render:

```text
https://<servico-staging>.onrender.com
```

### Produção

Branch responsável:

```text
main
```

Banco recomendado:

```text
inovagab_production
```

URL após configuração no Render:

```text
https://<servico-producao>.onrender.com
```

Os dois serviços usam o mesmo Dockerfile, mas possuem variáveis e bancos separados.

---

## Prints do funcionamento

As evidências reais devem ser adicionadas em:

```text
docs/evidencias/
```

### Evidência 1 — Pipeline: build e testes

Inserir:

```text
docs/evidencias/01-pipeline-build-testes.png
```

### Evidência 2 — Deploy Staging

Inserir:

```text
docs/evidencias/02-deploy-staging.png
```

### Evidência 3 — Staging funcionando

Inserir:

```text
docs/evidencias/03-staging-health.png
```

### Evidência 4 — Deploy Produção

Inserir:

```text
docs/evidencias/04-deploy-producao.png
```

### Evidência 5 — Produção funcionando

Inserir:

```text
docs/evidencias/05-producao-health.png
```

> As imagens precisam ser prints reais da execução. Não foram adicionadas evidências simuladas ao projeto.

---

## Tecnologias utilizadas

| Categoria | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 4.1.1 |
| API | Spring Web MVC |
| Segurança | Spring Security + JWT |
| Persistência | Spring Data MongoDB |
| Banco | MongoDB 7 / MongoDB Atlas |
| Validação | Bean Validation |
| IA | Google Gemini API |
| Build | Maven / Maven Wrapper |
| Containerização | Docker |
| Orquestração | Docker Compose |
| CI/CD | GitHub Actions |
| Deploy | Render |
| Controle de versão | Git / GitHub |

---

## Estrutura do projeto

```text
InovaGAB-Backend-main/
├── .github/
│   └── workflows/
│       └── ci-cd.yml
├── docs/
│   ├── DEPLOY.md
│   └── evidencias/
│       └── README.md
├── scripts/
│   └── smoke-test.sh
├── src/
│   ├── main/
│   └── test/
├── .dockerignore
├── .env.example
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

## Desafios encontrados e soluções

### Gerenciamento de segredos

**Desafio:** a aplicação utiliza MongoDB, JWT e opcionalmente Gemini, portanto credenciais não podem ficar no código.

**Solução:** configuração via variáveis de ambiente, `.env.example`, secrets dos environments do GitHub e variáveis protegidas no Render.

### Dependência do banco durante os testes

**Desafio:** o contexto Spring acessa o MongoDB durante a inicialização.

**Solução:** o pipeline inicia um MongoDB 7 isolado como `service container` antes da execução dos testes.

### Separação de staging e produção

**Desafio:** evitar que uma mudança seja enviada diretamente para o ambiente final.

**Solução:** branch `develop` para staging e `main` para produção, cada uma com ambiente, URL, banco e Deploy Hook próprios.

### Garantia após deploy

**Desafio:** um deploy pode terminar sem garantir que a aplicação realmente esteja respondendo.

**Solução:** endpoint público `/health` e validação automática após cada deploy.

---

## Checklist de entrega

> Marcar os dois últimos itens somente depois de executar o pipeline e salvar os prints reais.

| Item | OK |
|---|:---:|
| Projeto compactado em `.ZIP` com estrutura organizada | ✅ |
| Dockerfile funcional criado | ✅ |
| `docker-compose.yml` com aplicação + MongoDB | ✅ |
| Volume, variáveis de ambiente e rede configurados | ✅ |
| Pipeline com build, teste e deploy | ✅ |
| README.md com instruções técnicas | ✅ |
| `.env.example` sem segredos reais | ✅ |
| Documentação técnica em PPT | ✅ |
| Deploy realizado em staging | ☐ |
| Deploy realizado em produção | ☐ |
| Prints reais do pipeline e ambientes adicionados | ☐ |

---

## Aplicação original

O backend oferece autenticação JWT, perfis Operador/Gestor/Líder, CRUD de estratégias, ideias e projetos, dashboard executivo e integração opcional com Gemini para análise de ideias.

### Usuários de demonstração

| Perfil | E-mail | Senha |
|---|---|---|
| Líder | `lider@inovagab.com` | `123456` |
| Gestor | `gestor@inovagab.com` | `123456` |
| Operador | `operador@inovagab.com` | `123456` |

> Credenciais de demonstração são adequadas apenas para o ambiente acadêmico. Em produção real, devem ser alteradas.
