package AndroidChat;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.josurubio.itandem.R;
import com.firebase.client.Query;

/**
 * Created by josu on 01/07/15.
 */
public class GroupChatListAdapter extends FirebaseListAdapter<Chat> {
    private TextView CHAT_TXT;
    String authorID = "";

    /**
     * @param mRef        The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                    combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>,
     * @param mLayout     This is the mLayout used to represent a single list item. You will be responsible for populating an
     *                    instance of the corresponding view with the data from an instance of mModelClass.
     * @param activity    The activity containing the ListView
     */
    public GroupChatListAdapter(Query mRef, int mLayout, Activity activity) {
        super(mRef, Chat.class, mLayout, activity);
    }


    @Override
    protected void populateView(View v, Chat chat) {
        CHAT_TXT = (TextView)v.findViewById(R.id.message2);
        CHAT_TXT.setText(chat.getAuthor() + "\n" + chat.getMessage() + "  -  " + chat.getDate());
    }
}
