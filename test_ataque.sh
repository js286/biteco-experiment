#!/bin/bash

echo "═══════════════════════════════════════════════════"
echo "   🎯 SIMULACIÓN DE ATAQUE - CONFIDENCIALIDAD"
echo "═══════════════════════════════════════════════════"
echo ""

# Víctima crea reporte
TOKEN_VICTIMA=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"carlos.retail","password":"Retail2025!"}' | jq -r '.token')

CREATE=$(curl -s -X POST http://localhost:8080/api/reports \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN_VICTIMA" \
  -d '{}')

REPORTE_ID=$(echo $CREATE | jq -r '.reporte.id')
EMPRESA_VICTIMA=$(echo $CREATE | jq -r '.reporte.companyId')

echo "📊 VÍCTIMA: Reporte ID $REPORTE_ID creado en $EMPRESA_VICTIMA"
echo "💰 Total real: $(echo $CREATE | jq -r '.reporte.totalCost')"
echo ""

# Atacante intenta ver
TOKEN_ATACANTE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"ana.fintech","password":"Fintech2025!"}' | jq -r '.token')

RESULTADO=$(curl -s -X GET "http://localhost:8080/api/reports/$REPORTE_ID" \
  -H "Authorization: Bearer $TOKEN_ATACANTE")

DATOS_ATACANTE=$(echo $RESULTADO | jq -r '.reporte.totalCost')

echo "🎭 ATACANTE: Intenta ver el reporte..."
echo "🔒 Lo que ve: $DATOS_ATACANTE"
echo ""

if [[ "$DATOS_ATACANTE" == "🔒 [ENCRIPTADO - No autorizado]" ]]; then
    echo "✅ ATAQUE FRUSTRADO - Confidencialidad protegida"
else
    echo "❌ VULNERABILIDAD - El atacante vio datos reales"
fi
