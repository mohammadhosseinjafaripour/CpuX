package com.cpux;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cpux.R;


/**
 * A fragment that launches other parts of the demo application.
 */
public class PageAbout extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        // Demonstration of a collection-browsing activity.
        rootView.findViewById(R.id.bt_read).setOnClickListener(new View.OnClickListener() {
        	@Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setData(Uri.parse("bazaar://details?id=com.cpux"));
                intent.setPackage("com.farsitel.bazaar");
                startActivity(intent);
            }
        });


        return rootView;
    }
}
