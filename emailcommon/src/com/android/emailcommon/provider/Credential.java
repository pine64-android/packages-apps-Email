package com.android.emailcommon.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.emailcommon.provider.EmailContent;
import com.android.emailcommon.utility.Utility;
import com.google.common.base.Objects;

public class Credential extends EmailContent implements Parcelable {

    public static final String TABLE_NAME = "Credential";
    public static Uri CONTENT_URI;

    public static final Credential EMPTY = new Credential(-1, "", "", 0);

    public static void initCredential() {
        CONTENT_URI = Uri.parse(EmailContent.CONTENT_URI + "/credential");
    }

    public static final String TYPE_OAUTH = "oauth";

    public String mAccessToken;
    public String mRefreshToken;
    public long mExpiration;

    // Name of the authentication provider.
    public static final String PROVIDER_COLUMN = "provider";
    // Access token.
    public static final String ACCESS_TOKEN_COLUMN = "accessToken";
    // Refresh token.
    public static final String REFRESH_TOKEN_COLUMN = "refreshToken";
    // Expiration date for these credentials.
    public static final String EXPIRATION_COLUMN = "expiration";


    public interface CredentialQuery {
        public static final int ID_COLUMN_INDEX = 0;
        public static final int PROVIDER_COLUMN_INDEX = 1;
        public static final int ACCESS_TOKEN_COLUMN_INDEX = 2;
        public static final int REFRESH_TOKEN_COLUMN_INDEX = 3;
        public static final int EXPIRATION_COLUMN_INDEX = 4;

        public static final String[] PROJECTION = new String[] {
            RECORD_ID,
            PROVIDER_COLUMN,
            ACCESS_TOKEN_COLUMN,
            REFRESH_TOKEN_COLUMN,
            EXPIRATION_COLUMN
        };
    }

    public Credential() {
        mBaseUri = CONTENT_URI;
    }

    public Credential(long id, String accessToken, String refreshToken, long expiration) {
        mBaseUri = CONTENT_URI;
        mId = id;
        mAccessToken = accessToken;
        mRefreshToken = refreshToken;
        mExpiration = expiration;
    }

    /**
     * Restore a Credential from the database, given its unique id
     * @param context
     * @param id
     * @return the instantiated Credential
     */
   public static Credential restoreCredentialsWithId(Context context, long id) {
       return EmailContent.restoreContentWithId(context, Credential.class,
               Credential.CONTENT_URI, CredentialQuery.PROJECTION, id);
   }

   @Override
   public void restore(Cursor cursor) {
       mBaseUri = CONTENT_URI;
       mId = cursor.getLong(CredentialQuery.ID_COLUMN_INDEX);
       mAccessToken = cursor.getString(CredentialQuery.ACCESS_TOKEN_COLUMN_INDEX);
       mRefreshToken = cursor.getString(CredentialQuery.REFRESH_TOKEN_COLUMN_INDEX);
       mExpiration = cursor.getInt(CredentialQuery.EXPIRATION_COLUMN_INDEX);
   }

   /**
    * Supports Parcelable
    */
   @Override
   public int describeContents() {
       return 0;
   }

   /**
    * Supports Parcelable
    */
   public static final Parcelable.Creator<Credential> CREATOR
           = new Parcelable.Creator<Credential>() {
       @Override
       public Credential createFromParcel(Parcel in) {
           return new Credential(in);
       }

       @Override
       public Credential[] newArray(int size) {
           return new Credential[size];
       }
   };

   @Override
   public void writeToParcel(Parcel dest, int flags) {
       // mBaseUri is not parceled
       dest.writeLong(mId);
       dest.writeString(mAccessToken);
       dest.writeString(mRefreshToken);
       dest.writeLong(mExpiration);
   }

   /**
    * Supports Parcelable
    */
   public Credential(Parcel in) {
       mBaseUri = CONTENT_URI;
       mId = in.readLong();
       mAccessToken = in.readString();
       mRefreshToken = in.readString();
       mExpiration = in.readLong();
   }

   @Override
   public boolean equals(Object o) {
       if (!(o instanceof Credential)) {
           return false;
       }
       Credential that = (Credential)o;
       return Utility.areStringsEqual(mAccessToken, that.mAccessToken)
               && Utility.areStringsEqual(mRefreshToken, that.mRefreshToken)
               && mExpiration == that.mExpiration;
   }

   @Override
   public int hashCode() {
       return Objects.hashCode(mAccessToken, mRefreshToken, mExpiration);
   }

   @Override
   public ContentValues toContentValues() {
       ContentValues values = new ContentValues();
       values.put(ACCESS_TOKEN_COLUMN, mAccessToken);
       values.put(REFRESH_TOKEN_COLUMN, mRefreshToken);
       values.put(EXPIRATION_COLUMN, mExpiration);
       return values;
   }

}
