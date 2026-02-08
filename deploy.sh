#!/usr/bin/env bash
set -euo pipefail

cd /home/ubuntu/Smakers-BE

docker compose up -d --build

# nginx(80) 기준으로 health가 200 나올 때까지 대기
for i in {1..30}; do
  CODE="$(curl -s -o /dev/null -w "%{http_code}" http://localhost/health || true)"
  if [ "$CODE" = "200" ]; then
    echo "✅ Health OK (200)"
    echo "✅ Deploy success"
    exit 0
  fi
  echo "⏳ waiting... ($i/30) health code=$CODE"
  sleep 2
done

echo "❌ Deploy failed: health not ready"
exit 1
