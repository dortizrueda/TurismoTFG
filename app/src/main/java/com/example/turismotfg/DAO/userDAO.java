package com.example.turismotfg.DAO;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.turismotfg.Entity.User;
import com.example.turismotfg.MainActivity;
import com.example.turismotfg.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class userDAO {
    private static final String SHARE_PREFS="shared_prefs";
    private static final String EMAIL_KEY="email";
    private static final String PASSWORD_KEY="password";
    private static final String NAME_KEY="name";
    private static final String SURNAME_KEY="surname";
    private static final String ROL_KEY="role";
    private FirebaseFirestore firestore;
    private FirebaseUser user;
    private Context context;

    public void addUser(User user, String userId,Context context){
        CollectionReference usersRef = FirebaseFirestore.getInstance().collection("users");
        usersRef.document(userId).set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Registro exitoso!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    // Manejar errores durante el registro en Firestore
                    Toast.makeText(context, "Error al registrar el usuario en Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    public void editSurnameDAO(String name,String userId,Context context){
        DocumentReference ref_user=FirebaseFirestore.getInstance().collection("users").document(userId);
        ref_user.update("surname",name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "Surname de usuario actualizado en Firestore.");
                        Toast.makeText(context,"Surname de usuario actualizado en Firestore.",Toast.LENGTH_SHORT).show();

                        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(SURNAME_KEY,name);
                        editor.apply();

                        Intent intent = new Intent(context, UserProfile.class);
                        context.startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Error al actualizar el user.");
                    }
                });

    }
    public void editNameDAO(String name,String id,Context context){
        DocumentReference ref_user=FirebaseFirestore.getInstance().collection("users").document(id);
        ref_user.update("name",name)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "Nombre de usuario actualizado en Firestore.");
                        Toast.makeText(context,"Nombre de usuario actualizado en Firestore.",Toast.LENGTH_SHORT).show();

                        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(NAME_KEY,name);
                        editor.apply();

                        Intent intent = new Intent(context, UserProfile.class);
                        context.startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Error al actualizar el user.");
                    }
                });

    }
    public void editPasswordDAO(String password,Context context){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared_prefs", MODE_PRIVATE);
        String old_password = sharedPreferences.getString(PASSWORD_KEY, "");
        AuthCredential credential = EmailAuthProvider.getCredential(email, old_password);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Usuario reautenticado correctamente.");

                    // Actualizar contraseña en Firebase Auth
                    user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "Contraseña actualizada con éxito en Firebase Auth.");

                                // Actualizar contraseña en Firestore
                                DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(user.getUid());
                                userRef.update("password", password).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Aquí puedes manejar cualquier lógica adicional después de actualizar la contraseña
                                        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(PASSWORD_KEY,password);
                                        editor.apply();
                                        Log.d("TAG", "Contraseña actualizada con éxito en Firestore.");
                                        Intent intent = new Intent(context, UserProfile.class);
                                        context.startActivity(intent);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("TAG", "Error al actualizar la contraseña en Firestore.", e);
                                    }
                                });
                            } else {
                                Log.e("TAG", "Error al actualizar la contraseña en Firebase Auth.", task.getException());
                            }
                        }
                    });
                } else {
                    Log.e("TAG", "Error al reautenticar al usuario.", task.getException());
                }
            }
        });
    }

}
