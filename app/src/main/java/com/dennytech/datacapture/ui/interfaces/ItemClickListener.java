package com.dennytech.datacapture.ui.interfaces;

import com.google.firebase.firestore.DocumentSnapshot;

public interface ItemClickListener {
    void onItemClick(DocumentSnapshot documentSnapshot, int position);
}
