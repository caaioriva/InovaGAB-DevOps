#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${1:-http://localhost:8080}"

echo "[1/2] Health check: $BASE_URL/health"
curl -fsS "$BASE_URL/health"
echo

echo "[2/2] Login de teste"
curl -fsS -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"gestor@inovagab.com","senha":"123456"}'
echo

echo "Smoke test concluído."
