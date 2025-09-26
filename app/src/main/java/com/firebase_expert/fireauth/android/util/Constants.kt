package com.firebase_expert.fireauth.android.util

//App
const val TAG = "AppTag"

//Preview
const val LIGHT_MODE = "Light Mode"
const val DARK_MODE = "Dark Mode"

//Testing Phone Credentials
const val TEST_COUNTRY_CODE = "+40"
const val TEST_PHONE_NUMBER = "(701)234-567"
const val TEST_CODE = "123456"

//Auth Messages
const val SIGN_IN_LINK_SENT_SUCCESS = "Sign-in link successfully sent to email"
const val SIGN_IN_LINK_SENT_FAILURE = "Failed to send sign-in link to email"
const val SIGN_IN_WITH_EMAIL_LINK_SUCCESS = "Signed in successfully with email link"
const val SIGN_IN_WITH_EMAIL_LINK_FAILURE = "Failed to sign in with email link."
const val REAUTHENTICATION_LINK_SENT_SUCCESS = "Reauthentication link successfully sent to email"
const val REAUTHENTICATION_LINK_SENT_FAILURE = "Failed to send reauthentication link to email"
const val REAUTHENTICATION_LINK_NOT_SENT = "Reauthentication link not sent"
const val REAUTHENTICATION_WITH_EMAIL_LINK_SUCCESS = "Reauthenticated successfully with email link"
const val REAUTHENTICATION_WITH_EMAIL_LINK_FAILURE = "Failed to reauthenticate with email link"
const val SIGN_OUT_SUCCESS = "Signed out successfully"
const val DELETE_ACCOUNT_SUCCESS = "Account deleted successfully"
const val DELETE_ACCOUNT_FAILURE = "Failed to delete account"
const val SENSITIVE_OPERATION = "This operation is sensitive and requires reauthentication"
const val PHONE_NUMBER_VERIFICATION_SUCCESS = "Phone number successfully verified"
const val PHONE_NUMBER_VERIFICATION_FAILURE = "Failed to verify the phone number"
const val SIGN_IN_WITH_PHONE_SUCCESS = "Signed in successfully with phone"
const val SIGN_IN_WITH_PHONE_FAILURE = "Failed to sign in with phone"
const val REAUTHENTICATION_WITH_PHONE_SUCCESS = "Reauthenticated successfully with phone"
const val REAUTHENTICATION_WITH_PHONE_FAILURE = "Failed to reauthenticate with phone"
const val INVALID_CREDENTIALS = "Invalid credentials"
const val TOO_MANY_REQUESTS = "The SMS quota for the project has been exceeded"
const val MISSING_ACTIVITY_FOR_RECAPTCHA = "reCAPTCHA verification failed"
const val VERIFICATION_CODE_SENT_SUCCESS = "Verification code successfully sent to phone"
const val VERIFICATION_CODE_RESENT_SUCCESS = "Verification code successfully resent to phone"
const val VERIFICATION_CODE_NOT_SENT = "Verification code not sent to phone"
const val INVALID_EMAIL = "Invalid email"
const val INVALID_EMAIL_LINK = "The link in the email is invalid, expired, or has already been used"
const val INVALID_PHONE_NUMBER = "Invalid phone number"
const val UNKNOWN_FAILURE = "Unknown failure"

//SaleInfo Messages
const val FETCH_SALE_INFO_SUCCESS = "Sale info successfully fetched"
const val FETCH_SALE_INFO_FAILURE = "Failed to fetch sale info"

//Realtime Database Nodes
const val SALE_INFO = "saleInfo"

//Action Labels
const val SEND_EMAIL = "Send email?"
const val SEND_CODE = "Send code?"

//Country Phone Codes
val COUNTRIES = mapOf(
    "China" to "41",
    "France" to "33",
    "Germany" to "49",
    "Great Britain" to "44",
    "India" to "91",
    "Italy" to "39",
    "Japan" to "81",
    "Netherlands" to "31",
    "Romania" to "40",
    "Spain" to "34",
    "United States" to "1"
)