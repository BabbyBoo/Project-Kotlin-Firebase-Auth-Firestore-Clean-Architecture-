package com.example.myapplication.navigation

object NavRoutes {

    const val SIGN_IN = "sign_in"
    const val SIGN_UP = "sign_up"

    // Profile có tham số UID
    const val PROFILE = "profile"
    const val ARG_UID = "uid"
    const val PROFILE_WITH_UID = "$PROFILE/{$ARG_UID}"
}
