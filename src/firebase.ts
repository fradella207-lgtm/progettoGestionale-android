import { initializeApp, getApps, getApp } from 'firebase/app';
import { 
  getFirestore, 
  collection, 
  doc, 
  setDoc, 
  getDoc, 
  getDocs, 
  onSnapshot, 
  query, 
  where, 
  orderBy,
  deleteDoc
} from 'firebase/firestore';
import { 
  getAuth, 
  signInWithPopup, 
  GoogleAuthProvider, 
  signInWithEmailAndPassword, 
  createUserWithEmailAndPassword, 
  sendPasswordResetEmail,
  signOut,
  onAuthStateChanged,
  User as FirebaseUser
} from 'firebase/auth';
import firebaseConfig from '../firebase-applet-config.json';

// Initialize Firebase App
const app = !getApps().length ? initializeApp(firebaseConfig) : getApp();

// Initialize Firestore
const db = getFirestore(app, firebaseConfig.firestoreDatabaseId || undefined);

// Initialize Auth
const auth = getAuth(app);
const googleProvider = new GoogleAuthProvider();
googleProvider.setCustomParameters({
  prompt: 'select_account'
});

/**
 * Helper to translate Firebase Auth errors into friendly Italian messages
 */
export function getFirebaseAuthErrorMessage(errorCode: string, defaultMsg?: string): string {
  switch (errorCode) {
    case 'auth/user-not-found':
      return 'Nessun account trovato con questo indirizzo email.';
    case 'auth/wrong-password':
    case 'auth/invalid-credential':
      return 'Credenziali non corrette. Verifica email e password o reimposta la password.';
    case 'auth/email-already-in-use':
      return 'Esiste già un account registrato con questo indirizzo email. Effettua l\'accesso.';
    case 'auth/invalid-email':
      return 'Indirizzo email non valido.';
    case 'auth/weak-password':
      return 'La password è troppo debole. Inserisci almeno 6 caratteri.';
    case 'auth/missing-email':
      return 'Inserisci un indirizzo email valido.';
    case 'auth/too-many-requests':
      return 'Troppi tentativi non riusciti. L\'accesso è temporaneamente bloccato per sicurezza. Riprova più tardi o reimposta la password.';
    case 'auth/popup-closed-by-user':
    case 'auth/cancelled-popup-request':
      return 'Accesso con Google annullato dalla chiusura della finestra.';
    case 'auth/popup-blocked':
      return 'La finestra di accesso Google è stata bloccata dal browser. Consenti i popup per continuare.';
    case 'auth/network-request-failed':
      return 'Errore di connessione di rete. Verifica la tua connessione Internet e riprova.';
    default:
      return defaultMsg || 'Si è verificato un errore durante l\'operazione. Riprova.';
  }
}

/**
 * Send password reset email with Firebase Auth
 */
export async function sendPasswordReset(email: string): Promise<{ success: boolean; message: string }> {
  try {
    const cleanEmail = email.trim();
    if (!cleanEmail) {
      return { success: false, message: 'Inserisci un indirizzo email valido.' };
    }
    await sendPasswordResetEmail(auth, cleanEmail);
    return { 
      success: true, 
      message: `Abbiamo inviato un'email di ripristino all'indirizzo ${cleanEmail}. Controlla la tua casella di posta (inclusa la cartella Spam) per impostare una nuova password.` 
    };
  } catch (error: any) {
    const msg = getFirebaseAuthErrorMessage(error.code, error.message);
    return { success: false, message: msg };
  }
}

export {
  app,
  db,
  auth,
  googleProvider,
  signInWithPopup,
  signInWithEmailAndPassword,
  createUserWithEmailAndPassword,
  sendPasswordResetEmail,
  signOut,
  onAuthStateChanged,
  collection,
  doc,
  setDoc,
  getDoc,
  getDocs,
  onSnapshot,
  query,
  where,
  orderBy,
  deleteDoc
};
export type { FirebaseUser };
