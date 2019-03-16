package com.mxfit.mentix.menu3.LoginPackage;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mxfit.mentix.menu3.FireBaseConnection;
import com.mxfit.mentix.menu3.MainActivity;
import com.mxfit.mentix.menu3.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    MainActivity ma;
    private Button btnChangePassword, btnRemoveUser,
            changePassword, remove, signOut;
    private TextView email;
    private TextView name;
    private boolean removeVisible = false;

    public AccountFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public AccountFragment(MainActivity ma) {
        this.ma = ma;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signOut = (Button) view.findViewById(R.id.sign_out);
        email = (TextView) view.findViewById(R.id.useremail);

        name = (TextView) view.findViewById(R.id.username);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ma.signOut();
            }
        });

        if(FireBaseConnection.user != null)
            email.setText("E-mail: " + FireBaseConnection.user.getEmail());

        remove = (Button) view.findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(removeVisible){
                    removeVisible = false;
                    btnRemoveUser.setVisibility(View.GONE);
                }
                else{
                    removeVisible = true;
                    btnRemoveUser.setVisibility(View.VISIBLE);
                }
            }
        });

        btnRemoveUser = (Button) view.findViewById(R.id.remove_user_button);
        btnRemoveUser.setVisibility(View.GONE);
        btnRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.DeleteAcc_Message)
                        .setTitle(R.string.DeleteAcc_Title);
                builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (FireBaseConnection.user != null) {
                            FireBaseConnection.dbAccount.child(FireBaseConnection.user.getUid()).removeValue();
                            FireBaseConnection.user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getActivity(), "Account Removed!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                                ma.finish();
                                            } else {
                                                Toast.makeText(getActivity(), "There was problem removing your account. Try again later!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
                builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
