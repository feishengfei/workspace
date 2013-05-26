package com.navchina.sms.model;

import android.net.Uri;
import android.provider.BaseColumns;

public class Sms
{
	public static final String AUTHORITH = "com.navchina.sms.provider.Sms";

	private Sms()
	{
	}

	public static final class BDMsgColumns implements BaseColumns
	{
		private BDMsgColumns()
		{
		}

		public static final String TABLE_NAME = "bdmsg";
		public static final Uri COUNTENT_URI = Uri.parse("content://"
				+ AUTHORITH + "/" + TABLE_NAME);
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.navchina."
				+ TABLE_NAME;
		public static final String CONTENT_ITEM_TYPE = "vnd.android.curosr.item/vnd.navchina."
				+ TABLE_NAME;

		public static final String NUM = "num";
		public static final String CONTENT = "content";
		public static final String TYPE = "type";
		public static final String TIME = "time";
		public static final String SERIALNUM = "serialnum";
		public static final String LON = "lon";
		public static final String LAT = "lat";

		public static final String BDMSG_INDEX_TYPE = TABLE_NAME + "_" + TYPE;
		public static final String DEFAULT_SORT_ORDER = TIME + " DESC";
	}

	public static final class ServMsgColumns implements BaseColumns
	{
		private ServMsgColumns()
		{
		}

		public static final String TABLE_NAME = "servmsg";
		public static final Uri COUNTENT_URI = Uri.parse("content://"
				+ AUTHORITH + "/" + TABLE_NAME);

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.navchina."
				+ TABLE_NAME;
		public static final String CONTENT_ITEM_TYPE = "vnd.android.curosr.item/vnd.navchina."
				+ TABLE_NAME;

		public static final String CONTENT = "content";
		public static final String TIME = "time";
		
		public static final String DEFAULT_SORT_ORDER = TIME + " DESC";
	}
}
