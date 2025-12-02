package com.example.bloom_app.ui.screen.auth  // ✅ CORRIGÉ

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await  // ✅ AJOUTÉ !
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    data class Error(val message: String) : AuthState()
    object Unauthenticated : AuthState()
}

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        _authState.value = if (auth.currentUser != null) {
            AuthState.Authenticated
        } else {
            AuthState.Unauthenticated
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                auth.signInWithEmailAndPassword(email, password).await()
                _authState.value = AuthState.Authenticated
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _authState.value = AuthState.Error("Email ou mot de passe incorrect")
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Erreur de connexion")
            }
        }
    }

    fun signUp(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            try {
                // ✅ VALIDATION Confirm Password
                if (password != confirmPassword) {
                    _authState.value = AuthState.Error("Les mots de passe ne correspondent pas")
                    return@launch
                }

                if (password.length < 6) {
                    _authState.value = AuthState.Error("Le mot de passe doit faire 6 caractères minimum")
                    return@launch
                }

                _authState.value = AuthState.Loading
                auth.createUserWithEmailAndPassword(email, password).await()
                _authState.value = AuthState.Authenticated
            } catch (e: FirebaseAuthUserCollisionException) {
                _authState.value = AuthState.Error("Cet email est déjà utilisé")
            } catch (e: FirebaseAuthWeakPasswordException) {
                _authState.value = AuthState.Error("Mot de passe trop faible")
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Erreur d'inscription")
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(credential).await()
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Échec Google Sign-In")
            }
        }
    }

    fun signOut() {
        auth.signOut()
        checkAuthStatus()
    }
}
