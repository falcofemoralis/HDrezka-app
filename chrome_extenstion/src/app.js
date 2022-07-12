const getDocumentId = (data) => {
    if (!data.userId || data.userId == '0') {
        throw Error("unauthorized")
    }
    return `${data.userId}-${data.titleId}-${data.voiceId}-${data.season}-${data.episode}`
};
const getDeviceId = () => {
    let id = localStorage.getItem('DEVICE_ID');

    if (!id) {
        id = Date.now();
        localStorage.setItem('DEVICE_ID', id);
    }

    return id;
}

firebase.initializeApp(firebaseConfig);
const db = firebase.firestore();

const handleFromWeb = async (event) => {
    if (event.data.action) {
        if (event.data.action == 'get') {
            try {
                const data = (await db.collection(DB_COLLECTION).doc(getDocumentId(event.data.data)).get()).data();
                if (data && getDeviceId() != data.deviceId) {
                    window.postMessage({ action: 'seek', data: { time: data.time } })
                }
            } catch (error) {
                console.log(error);
            }

            window.postMessage({ action: 'fetched' })
        } else if (event.data.action == 'update') {
            const { time } = event.data.data

            if (time > 0) {
                db.collection(DB_COLLECTION).doc(getDocumentId(event.data.data)).set({ time, deviceId: getDeviceId() })
            }
        }
    }
};
window.addEventListener('message', handleFromWeb);