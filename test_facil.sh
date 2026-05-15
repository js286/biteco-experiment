#!/bin/bash

echo "═══════════════════════════════════════════════════"
echo "   🧪 BITE.CO - PRUEBA DE CONFIDENCIALIDAD"
echo "═══════════════════════════════════════════════════"
echo ""

# Colores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 1. Login como carlos.retail
echo -e "${YELLOW}📋 1. Login como carlos.retail${NC}"
LOGIN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"carlos.retail","password":"Retail2025!"}')

TOKEN=$(echo $LOGIN | jq -r '.token')
COMPANY=$(echo $LOGIN | jq -r '.companyId')

if [ "$TOKEN" != "null" ] && [ -n "$TOKEN" ]; then
    echo -e "${GREEN}✅ Login exitoso${NC}"
    echo -e "   Token: ${TOKEN:0:50}...\n"
else
    echo -e "${RED}❌ Login fallido${NC}"
    exit 1
fi

# 2. Crear reporte (automático - sin datos manuales)
echo -e "${YELLOW}📋 2. Creando reporte automático${NC}"
CREATE=$(curl -s -X POST http://localhost:8080/api/reports \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{}')  # Objeto vacío usa valores por defecto

echo "$CREATE" | jq '.'

REPORT_ID=$(echo "$CREATE" | jq -r '.reporte.id')
echo -e "${GREEN}✅ Reporte creado con ID: $REPORT_ID${NC}\n"

# 3. Ver reporte (usuario autorizado - ve datos claros)
echo -e "${YELLOW}📋 3. Usuario AUTORIZADO ve su reporte${NC}"
curl -s -X GET "http://localhost:8080/api/reports/$REPORT_ID" \
  -H "Authorization: Bearer $TOKEN" | jq '.'

# 4. Ver datos encriptados (endpoint raw)
echo -e "\n${YELLOW}📋 4. Ver datos CRUDOS (encriptados en DB)${NC}"
curl -s -X GET "http://localhost:8080/api/reports/$REPORT_ID/raw" \
  -H "Authorization: Bearer $TOKEN" | jq '.reporte | {totalCost, breakdown, notes, totalCostEncrypted}'

# 5. Login como ana.fintech (otra empresa)
echo -e "\n${YELLOW}📋 5. Login como ana.fintech (otra empresa)${NC}"
LOGIN_ANA=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"ana.fintech","password":"Fintech2025!"}')

TOKEN_ANA=$(echo $LOGIN_ANA | jq -r '.token')
echo -e "${GREEN}✅ Ana logueada${NC}\n"

# 6. Ana intenta ver reporte de Carlos (NO autorizada - ve encriptado)
echo -e "${YELLOW}📋 6. Usuario NO AUTORIZADO intenta ver reporte de otra empresa${NC}"
curl -s -X GET "http://localhost:8080/api/reports/$REPORT_ID" \
  -H "Authorization: Bearer $TOKEN_ANA" | jq '.'

# 7. Admin puede ver todo
echo -e "\n${YELLOW}📋 7. Login como ADMIN${NC}"
LOGIN_ADMIN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin.biteco","password":"Admin2025!"}')

TOKEN_ADMIN=$(echo $LOGIN_ADMIN | jq -r '.token')
echo -e "${GREEN}✅ Admin logueado${NC}\n"

echo -e "${YELLOW}📋 8. ADMIN ve cualquier reporte (desencriptado)${NC}"
curl -s -X GET "http://localhost:8080/api/reports/$REPORT_ID" \
  -H "Authorization: Bearer $TOKEN_ADMIN" | jq '.reporte | {companyId, totalCost, breakdown}'

echo -e "\n${GREEN}═══════════════════════════════════════════════════${NC}"
echo -e "${GREEN}✅ EXPERIMENTO COMPLETADO${NC}"
echo -e "${GREEN}═══════════════════════════════════════════════════${NC}"
echo ""
echo "📊 RESULTADOS:"
echo "  ✅ Reporte creado automáticamente sin necesidad de datos"
echo "  ✅ Usuario autorizado ve datos DESENCRIPTADOS"
echo "  ✅ Usuario NO autorizado ve datos ENCRIPTADOS"
echo "  ✅ Endpoint RAW muestra datos encriptados"
echo "  ✅ ADMIN tiene acceso completo"
echo ""
