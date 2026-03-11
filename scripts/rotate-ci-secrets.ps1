param(
    [Parameter(Mandatory = $true)]
    [string]$Repo, # owner/repo

    [Parameter(Mandatory = $true)]
    [string]$PrivateKeyPath,

    [Parameter(Mandatory = $true)]
    [string]$PublicKeyPath,

    [Parameter(Mandatory = $true)]
    [string]$DbUsername,

    [Parameter(Mandatory = $true)]
    [string]$DbPassword,

    [Parameter(Mandatory = $true)]
    [string]$DbJdbcUrl,

    [string]$JwtIssuer = "udesc-api"
)

$ErrorActionPreference = "Stop"

if (-not (Get-Command gh -ErrorAction SilentlyContinue)) {
    throw "GitHub CLI (gh) nao encontrado. Instale e rode: gh auth login"
}

if (-not (Test-Path $PrivateKeyPath)) {
    throw "Private key nao encontrada: $PrivateKeyPath"
}
if (-not (Test-Path $PublicKeyPath)) {
    throw "Public key nao encontrada: $PublicKeyPath"
}

$privatePem = Get-Content -Raw -Path $PrivateKeyPath
$publicPem = Get-Content -Raw -Path $PublicKeyPath

Write-Host "Atualizando GitHub Secrets em $Repo..."
gh secret set DB_USERNAME --repo $Repo --body $DbUsername
gh secret set DB_PASSWORD --repo $Repo --body $DbPassword
gh secret set DB_JDBC_URL --repo $Repo --body $DbJdbcUrl
gh secret set JWT_ISSUER --repo $Repo --body $JwtIssuer
gh secret set JWT_PRIVATE_KEY_PEM --repo $Repo --body $privatePem
gh secret set JWT_PUBLIC_KEY_PEM --repo $Repo --body $publicPem

Write-Host "Segredos atualizados com sucesso."
Write-Host "No workflow, gere arquivos temporarios e exporte:"
Write-Host "JWT_PRIVATE_KEY_LOCATION e JWT_PUBLIC_KEY_LOCATION"
