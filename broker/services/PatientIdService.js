/**
 * Patient ID service class.
 * 
 * @author Todd Jensen
 * @author www.massvaxx.com
 */
const uuid = require('uuid');
const db = require('./database');
const PatientIdModel = db.patient_ids;
const QrCodeModel = db.qr_codes;

/**
 * Provides services related to creation of MassVaxx patient identifiers.
 */
class PatientIdService {

    /**
     * Returns new patient ID record with unique patient identifer in patient_id field.
     * 
     * @return patient ID record (patient_id value will be null if error).
     */
    async createPatientId() {
        try {
            const patientIdRecord = await PatientIdModel.create({
                assigner: 'massvaxx'
            });
            if (patientIdRecord) {
                return patientIdRecord.toJSON();
            }
        }
        catch(e) {
            // +TODO+ Log error
            console.error(e);
        }
        return { patient_id: null };
    }

    /**
     * Returns new QR code record linked to the provided a patient ID with
     * unique string for QR code in qr_code field.
     * 
     * @param patientId simple patient identifier (e.g., "1" or "000001").
     * @return QR code record with (qr_code will be null if error).
     */
    async createQrCodeForPatientId(patientId) {
        try {
            const qrCodeRecord = await QrCodeModel.create({
                patient_id: patientId,
                qr_code: uuid.v4()
            });
            if (qrCodeRecord) {
                return { qr_code: qrCodeRecord.toJSON().qr_code };
            }
        }
        catch(e) {
            // +TODO+ Log error
            console.error(e);
        }
        return { qr_code: null };
    }

    async createQrCode(patientId) {
      return { qr_code: "12345" + patientId };
    }

    /**
     * Returns entire patient ID record for the provided a patient ID.
     * 
     * @param patientId simple patient identifier (e.g., "1" or "000001").
     * @return patient ID record (patient_id value will be null if error).
     */
    async getPatientId(patientId) {
        try {
            const patientIdRecord = await PatientIdModel.findOne({
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
            console.error(e);
        }
        return { patient_id: null };
    }

    /**
     * Returns entire patient ID record for the provided QR code string.
     * 
     * @param qrCode string from QR code (e.g., "ed3fdc7c-1aa4-4415-9256-942c6a1f5377").
     * @return patient ID record (patient_id value will be null if error).
     */
    async getPatientIdForQrCode(qrCode) {
        try {
            const patientIdRecord = await PatientIdModel.findOne({
                include: {
                    model: QrCodeModel,
                    as: 'qr_codes',
                    where: {
                        qr_code: qrCode
                    }
                }
            });
            if (patientIdRecord) {
                return patientIdRecord.toJSON();
            }
        }
        catch(e) {
            // +TODO+ Log error
            console.error(e);
            console.error(e);
        }
        return { patient_id: null };
    }
}

module.exports = new PatientIdService();
