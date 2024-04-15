package com.example.turismotfg.DAO;

import static android.accounts.AccountManager.KEY_PASSWORD;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import android.content.Context;

import com.example.turismotfg.Entity.Guide;
import com.example.turismotfg.Entity.Rol;
import com.example.turismotfg.Entity.User;
import com.example.turismotfg.MainActivity;
import com.example.turismotfg.UserActivity;
import com.example.turismotfg.UserProfile;
import com.example.turismotfg.UserRegister;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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
    public static class Email {
        public String email;
    }

    public userDAO(Context context) {
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void login(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        checkUser(email);
                    } else {
                        Toast toast = Toast.makeText(context,"Error durante el inicio de sesión: " + task.getException().getMessage(),Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
    }



    public void register(String email, String name, String surname, String password,Rol rol){
        // Crear usuario en Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (firebaseUser != null) {
                            User user = new User(email, name, surname, password,rol);
                            addUser(user,firebaseUser.getUid());
                        } else {
                            Toast.makeText(context, "Error: Usuario de Firebase nulo", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Error al registrar el usuario en Firebase: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUser(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        usersRef.whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);

                                if (user != null) {
                                    // Autenticación exitosa y se obtuvo la información del usuario
                                    Intent i;
                                    SharedPreferences sharedPreferences = context.getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString(EMAIL_KEY, user.getEmail());
                                    editor.putString(PASSWORD_KEY,user.getPassword());
                                    editor.putString(ROL_KEY, user.getRol().toString());
                                    editor.putString(NAME_KEY,user.getName());
                                    editor.putString(SURNAME_KEY,user.getSurname());
                                    editor.putBoolean("active", true);
                                    editor.apply();

                                    if (TextUtils.equals(user.getRol().toString(), Rol.admin.toString())) {
                                        // Usuario administrador, redirecciona a la actividad de administrador
                                        i = new Intent(context, MainActivity.class);
                                    } else {
                                        // No es administrador, redirecciona a la actividad de usuario normal
                                        i = new Intent(context, UserActivity.class);
                                    }

                                    // Pasa el objeto del usuario como extra al intent
                                    i.putExtra("USER_OBJECT_EXTRA", user);
                                    context.startActivity(i);
                                } else {
                                    // El objeto User es nulo, manejar el caso según tus necesidades
                                    Toast toast = Toast.makeText(context, "Error al obtener información del usuario", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        } else {
                            // Error al obtener datos de Firestore
                            Toast toast = Toast.makeText(context, "Error al obtener información del usuario proveniente de la BD.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });

    }
private void addUser(User user, String userId){
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
    public void getGuideId(String name, OnCompleteListener<QuerySnapshot> onCompleteListener) {
        FirebaseFirestore.getInstance().collection("guides")
                .whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }
    public void editName(String name){
        String userId=user.getUid();
        DocumentReference ref_user=FirebaseFirestore.getInstance().collection("users").document(user.getUid());
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

    /*public void editEmail(String name) {
        String email = user.getEmail();
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared_prefs", MODE_PRIVATE);
        String password = sharedPreferences.getString(PASSWORD_KEY, "");
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Usuario reautenticado correctamente.");
                    user.verifyBeforeUpdateEmail(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Correo electrónico cambiado con éxito", Toast.LENGTH_SHORT).show();

                                // Actualiza el correo electrónico en Firestore
                                DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(user.getUid());
                                userRef.update("email", name).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(EMAIL_KEY,name);
                                        editor.apply();
                                        Log.d("TAG", "Correo electrónico actualizado en Firestore.");
                                        Intent intent = new Intent(context, UserProfile.class);
                                        context.startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("TAG", "Error al actualizar el correo electrónico en Firestore.", e);
                                    }
                                });
                            } else {
                                Toast.makeText(context, "Error al cambiar el correo electrónico", Toast.LENGTH_SHORT).show();
                                Log.e("TAG", "Error al cambiar el correo electrónico", task.getException());
                            }
                        }
                    });
                } else {
                    Toast.makeText(context, "Error al reautenticar al usuario", Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "Error al reautenticar al usuario", task.getException());
                }
            }
        });
    }
*/
    public void editPassword(String password){
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

    public void editSurname(String name){
        String userId=user.getUid();
        DocumentReference ref_user=FirebaseFirestore.getInstance().collection("users").document(user.getUid());
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
public void removeGuideFromFavs(Guide guide,OnCompleteListener<Void> onCompleteListener){
        String name= guide.getName();
        if (user!=null){
            getGuideId(name,task -> {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        String guideId = document.getId();
                        FirebaseFirestore.getInstance().collection("users")
                                .document(user.getUid())
                                .get()
                                .addOnSuccessListener(document1->{
                                    List<DocumentReference>favorites=(List<DocumentReference>) document1.get("favorites");
                                    if (favorites!=null){
                                        for (DocumentReference i:favorites){
                                            i.get().addOnSuccessListener(document2->{
                                               String id_fav=document2.getId();
                                               if (id_fav.equals(guideId)){
                                                    favorites.remove(i);
                                                    document1.getReference().update("favorites",favorites)
                                                            .addOnCompleteListener(onCompleteListener)
                                                            .addOnSuccessListener(aVoid->{
                                                                Toast.makeText(context,"Guia eliminada de favoritas",Toast.LENGTH_SHORT).show();
                                                            })
                                                            .addOnFailureListener(exception->{
                                                                Toast.makeText(context,"Error al eliminar la guia de favoritos",Toast.LENGTH_SHORT).show();
                                                            });
                                               }
                                            });
                                        }
                                    }
                                })
                                .addOnFailureListener(exception->{
                                   Log.d("UserDAO","Error al obtener datos");
                                });
                    }
                }else {
                    Log.d("UserDAO","Error al obtener datos");
                }
            });
        }
}
    public void addGuideToUserFavs(Guide guide, OnCompleteListener<Void> onCompleteListener) {
        String name = guide.getName();
        if (user != null) {
            firestore.collection("guides").whereEqualTo("name", name)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String id = document.getId();
                                    DocumentReference guideRef = firestore.collection("guides").document(id);
                                    DocumentReference userRef = firestore.collection("users").document(user.getUid());
                                    userRef.get().addOnSuccessListener(documentSnapshot -> {
                                        List<DocumentReference> favorites = (List<DocumentReference>) documentSnapshot.get("favorites");
                                        if (favorites == null) {
                                            favorites = new ArrayList<>();
                                        }
                                        if (!favorites.contains(guideRef)) {
                                            favorites.add(guideRef);
                                            userRef.update("favorites", favorites)
                                                    .addOnCompleteListener(onCompleteListener)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(context, "Guía agregada a favoritos", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.e("UserDAO", "Error al agregar guía a favoritos", e);
                                                            Toast.makeText(context, "Error al agregar guía a favoritos", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }).addOnFailureListener(e -> Log.e("UserDAO", "Error al obtener datos del usuario", e));
                                }
                            } else {
                                Log.e("UserDAO", "Error al buscar la guía en la base de datos", task.getException());
                            }
                        }
                    });
        }
    }
}
