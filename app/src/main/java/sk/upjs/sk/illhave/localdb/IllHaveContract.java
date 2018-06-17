package sk.upjs.sk.illhave.localdb;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public interface IllHaveContract {

    String AUTHORITY = "sk.upjs.sk.illhave";

    interface Order extends BaseColumns {
        String TABLE_NAME = "restaurant_order";

        String API_ID = "api_id";

        String CREATED_DATE = "created_date";

        String TOTAL = "total";

        Uri CONTENT_URI = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

}