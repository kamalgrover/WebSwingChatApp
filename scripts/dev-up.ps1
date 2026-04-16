$ErrorActionPreference = 'Stop'

$RootDir = Resolve-Path (Join-Path $PSScriptRoot '..')
Set-Location $RootDir

function Fail([string]$Message) {
    Write-Host ""
    Write-Host "ERROR: $Message"
    Write-Host ""
    exit 1
}

Write-Host "[preflight] Checking required files..."

if (-not (Test-Path (Join-Path $RootDir 'webswing.zip'))) {
    Fail @"
Missing webswing.zip in project root.
Fix:
  1) Download Webswing 26.1 distribution zip.
  2) Place it at: $RootDir\webswing.zip
  3) Re-run: .\scripts\dev-up.ps1
"@
}

Write-Host "[preflight] All checks passed."
Write-Host "[docker] Starting compose build and run (multi-stage default)..."

docker-compose up --build
