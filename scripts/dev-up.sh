#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

fail() {
  echo ""
  echo "ERROR: $1"
  echo ""
  exit 1
}

echo "[preflight] Checking required files..."

if [[ ! -f "webswing.zip" ]]; then
  fail "Missing webswing.zip in project root.
Fix:
  1) Download Webswing 26.1 distribution zip.
  2) Place it at: ${ROOT_DIR}/webswing.zip
  3) Re-run: ./scripts/dev-up.sh"
fi

echo "[preflight] All checks passed."
echo "[docker] Starting compose build and run (multi-stage default)..."

docker-compose up --build
