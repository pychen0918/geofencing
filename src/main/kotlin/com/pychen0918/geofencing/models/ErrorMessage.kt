package com.pychen0918.geofencing.models

import kotlinx.serialization.Serializable

@Serializable
data class ErrorMessage(val code: Int, val message: String)