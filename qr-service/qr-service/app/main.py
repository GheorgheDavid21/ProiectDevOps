from fastapi import FastAPI, HTTPException
from .models import GenerateQrRequest, GenerateQrResponse, ValidateQrRequest
from .qr_store import generate_qr, validate_qr
from prometheus_client import Counter, generate_latest, CONTENT_TYPE_LATEST
from starlette.responses import Response

QR_GENERATED = Counter("qr_generated_total", "Total number of generated QR codes")
QR_VALIDATED = Counter("qr_validated_total", "Total number of validated QR codes")

app = FastAPI(title="QR Service")

@app.get("/health")
def health():
    return {"status": "UP"}

@app.post("/qr/generate", response_model=GenerateQrResponse)
def generate(request: GenerateQrRequest):
    qr_code = generate_qr(request.transaction_id)
    QR_GENERATED.inc()
    return GenerateQrResponse(qr_code=qr_code)

@app.post("/qr/validate")
def validate(request: ValidateQrRequest):
    if not validate_qr(request.qr_code):
        raise HTTPException(status_code=400, detail="Invalid QR code")
    QR_VALIDATED.inc()
    return {"status": "VALIDATED"}

@app.get("/metrics")
def metrics():
    data = generate_latest()
    return Response(content=data, media_type=CONTENT_TYPE_LATEST)