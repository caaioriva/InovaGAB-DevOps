# Deploy em Staging e Produção

A solução usa **GitHub Actions** para CI/CD e **Render** como plataforma de deploy. O MongoDB em nuvem pode ser fornecido pelo **MongoDB Atlas**.

## 1. Criar dois serviços no Render

Crie dois Web Services apontando para o mesmo repositório e usando o `Dockerfile` da raiz:

- `inovagab-staging` — branch `develop`
- `inovagab-production` — branch `main`

Em cada serviço, configure o Health Check Path como `/health`.

## 2. Variáveis de ambiente no Render

### Staging

```text
MONGODB_URI=<URI do MongoDB Atlas>
MONGODB_DATABASE=inovagab_staging
JWT_SECRET=<segredo forte com no mínimo 32 caracteres>
GEMINI_API_KEY=<opcional>
GEMINI_MODEL=gemini-2.5-flash
```

### Produção

```text
MONGODB_URI=<URI do MongoDB Atlas>
MONGODB_DATABASE=inovagab_production
JWT_SECRET=<outro segredo forte>
GEMINI_API_KEY=<opcional>
GEMINI_MODEL=gemini-2.5-flash
```

Não coloque segredos no repositório.

## 3. Obter Deploy Hooks

Em cada serviço do Render, abra as configurações de Deploy Hook e copie a URL correspondente.

## 4. Configurar GitHub Environments e Secrets

No repositório, abra **Settings → Environments** e crie:

- `staging`
- `production`

Adicione os seguintes secrets:

### Environment `staging`

- `RENDER_DEPLOY_HOOK_STAGING`
- `STAGING_URL` — URL pública do serviço, sem barra no final

### Environment `production`

- `RENDER_DEPLOY_HOOK_PRODUCTION`
- `PRODUCTION_URL` — URL pública do serviço, sem barra no final

## 5. Fluxo de deploy

- Push para `develop` → build → testes → Docker build → deploy em **staging** → health check.
- Push para `main` → build → testes → Docker build → deploy em **produção** → health check.
- Pull Request → build + testes + Docker build, sem deploy.

## 6. Evidências obrigatórias

Após a primeira execução bem-sucedida, salve prints em `docs/evidencias/`:

1. GitHub Actions mostrando `Build, testes e Docker` concluído.
2. Job `Deploy - Staging` concluído.
3. `https://<staging>/health` retornando `status: UP`.
4. Job `Deploy - Produção` concluído.
5. `https://<producao>/health` retornando `status: UP`.
6. Opcional: login/API funcionando em staging e produção.
