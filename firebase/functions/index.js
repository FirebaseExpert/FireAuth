const functions = require("firebase-functions/v1");
const admin = require("firebase-admin");
admin.initializeApp();

const db = admin.firestore();
const storage = admin.storage();

const USERS = "users";
const IMAGES = "images";

exports.addUser = functions.auth.user().onCreate(async (user) => {
  const {uid, email, displayName, photoURL} = user;

  const uidRef = db.collection(USERS).doc(uid);

  try {
    await db.runTransaction(async (transaction) => {
      const userDoc = await transaction.get(uidRef);
      const userRecord = {
        ...(email && {email}),
        ...(displayName && {displayName}),
        ...(photoURL && {photoUrl: photoURL}),
      };
      if (!userDoc.exists) {
        transaction.set(uidRef, {
          ...userRecord,
          createdAt: admin.firestore.FieldValue.serverTimestamp(),
        });
      } else {
        transaction.update(uidRef, userRecord);
      }
    });
    return true;
  } catch (error) {
    console.error("Error adding user:", error);
    return null;
  }
});

exports.deleteUser = functions.auth.user().onDelete(async ({uid}) => {
  try {
    await db.collection(USERS).doc(uid).delete();

    const filePath = IMAGES + "/" + uid + ".jpg";
    const file = storage.bucket().file(filePath);
    const [exists] = await file.exists();
    if (exists) {
      await file.delete();
    } else {
      console.log("File does not exist: ${filePath}");
    }
    return true;
  } catch (error) {
    console.error("Error deleting user:", error);
    return null;
  }
});
