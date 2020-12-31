// Patient ID service.
//
const uuid = require('uuid');
const db = require('./database');
const PatientId = db.patient_ids;
const QrCode = db.qr_codes;

class PatientIdService {

    // Returns new patient ID record
    //
    async createPatientId() {
        try {
            const patientIdRecord = await PatientId.create({
                assigner: 'massvaxx'
            });
            if (patientIdRecord) {
                return patientIdRecord.toJSON();
            }
        }
        catch(e) {
            // +TODO+ Log error
        }
        return { patient_id: null };
    }

    // Returns new and unique QR code linked to the provided a patient ID.
    //
    async createQrCodeForPatientId(patientId) {
        try {
            const qrCodeRecord = await QrCode.create({
                patient_id: patientId,
                qr_code: uuid.v4()
            });
            if (qrCodeRecord) {
                return { qr_code: qrCodeRecord.toJSON().qr_code };
            }
        }
        catch(e) {
            // +TODO+ Log error
        }
        return { qr_code: null };
    }

    // Returns entire patient ID record corresponding to provided patient ID value.
    //
    async getPatientId(patientId) {
        try {
            const patientIdRecord = await PatientId.findOne({
                where: {
                    patient_id: patientId
                }
            });
            if (patientIdRecord) {
                return patientIdRecord.toJSON();
            }
        }
        catch(e) {
            // +TODO+ Log error
        }
        return { patient_id: null };
    }

    // Returns patient ID record corresponding to provided QR code.
    //
    async getPatientIdForQrCode(qrCode) {
        // Change to one query, e.g. "select patient_ids.* from patient_ids p, qr_codes q where p.patient_id=q.patient_id and q.qr_code=qrCode"
        //
        try {
            const qrCodeRecord = await QrCode.findOne({
                where: {
                    qr_code: qrCode
                }
            });
            if (qrCodeRecord)
            {
                const patient_id = qrCodeRecord.toJSON().patient_id;
                return this.getPatientId(patient_id);
            }
        }
        catch(e) {
            // +TODO+ Log error
            console.error(e);
        }
        return { patient_id: null };
    }
}

module.exports = new PatientIdService();