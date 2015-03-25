package datx021512.chalmers.se.greenme.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.plus.Plus;


import datx021512.chalmers.se.greenme.MainActivity;
import datx021512.chalmers.se.greenme.R;

/**
 * Created by Patrik on 2015-03-24.
 */
public class PlayFragment extends Fragment implements OnInvitationReceivedListener, OnTurnBasedMatchUpdateReceivedListener,
        View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;
    private Button mCreateButton;
    private Button mCheckButton;
    final static int TOAST_DELAY = Toast.LENGTH_SHORT;

    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_LOOK_AT_MATCHES = 10001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_play, container, false);

        TextView txt=(TextView)rootView.findViewById(R.id.txt);
        mGoogleApiClient = new GoogleApiClient.Builder(rootView.getContext().getApplicationContext())
                               .addConnectionCallbacks(this)
                               .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .build();

        mGoogleApiClient.connect();
        mCreateButton = (Button) rootView.findViewById(R.id.create_game_text);
        mCreateButton.setOnClickListener(this);
        mCheckButton = (Button) rootView.findViewById(R.id.check_game_text);
        mCheckButton.setOnClickListener(this);

        return rootView;
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }

    @Override
    public void onClick(View v) {
        Log.d("Testing", "onClick!");
        switch (v.getId()) {
            case R.id.create_game_text:
                Log.d("Testing", "Create Game button pressed!");
                Intent intent = Games.TurnBasedMultiplayer.getSelectOpponentsIntent(mGoogleApiClient,
                        1, 7, true);
                startActivityForResult(intent, RC_SELECT_PLAYERS);
                break;
            case R.id.check_game_text:
                Log.d("Testing","Check Game button pressed!");
                intent = Games.TurnBasedMultiplayer.getInboxIntent(mGoogleApiClient);
                startActivityForResult(intent, RC_LOOK_AT_MATCHES);
                break;
        }
    }

    @Override
    public void onInvitationReceived(Invitation invitation) {
        Log.d("Testing", "onInvitationReceived()");
        Toast.makeText(
                getActivity().getApplicationContext(),
                "An invitation has arrived from "
                        + invitation.getInviter().getDisplayName(), Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onInvitationRemoved(String s) {
        Log.d("Testing", "onInvitationRemoved()");
        Toast.makeText(getActivity().getApplicationContext(), "An invitation was removed.", TOAST_DELAY).show();
    }

    @Override
    public void onTurnBasedMatchReceived(TurnBasedMatch turnBasedMatch) {
        Log.d("Testing", "onTurnBasedMatchReceived()");
        Toast.makeText(getActivity().getApplicationContext(), "A match was updated.", TOAST_DELAY).show();
    }

    @Override
    public void onTurnBasedMatchRemoved(String s) {
        Log.d("Testing", "onTurnBasedMatchRemoved()");
        Toast.makeText(getActivity().getApplicationContext(), "A match was removed.", TOAST_DELAY).show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("Testing", "onConnected metoden!!!!!!!!" + mGoogleApiClient.isConnected());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Testing", "onConnectionSuspended metoden!!!!!!!!" + mGoogleApiClient.isConnected());
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("Testing", "onConnectionFailed metoden!!!!!!!!" + mGoogleApiClient.isConnected());
    }
}

