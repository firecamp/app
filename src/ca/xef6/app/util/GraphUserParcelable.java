package ca.xef6.app.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.model.GraphUser;

public class GraphUserParcelable implements Parcelable {

	public static final Parcelable.Creator<GraphUserParcelable> CREATOR = new Parcelable.Creator<GraphUserParcelable>() {

		@Override
		public GraphUserParcelable createFromParcel(Parcel source) {
			return new GraphUserParcelable(source);
		}

		@Override
		public GraphUserParcelable[] newArray(int size) {
			return new GraphUserParcelable[size];
		}

	};

	private final String birthday;
	private final String firstName;
	private final String id;
	private final String lastName;
	private final String link;
	private final String middleName;
	private final String name;
	private final String username;

	public GraphUserParcelable(GraphUser graphUser) {
		if (graphUser != null) {
			birthday = graphUser.getBirthday();
			firstName = graphUser.getFirstName();
			id = graphUser.getId();
			lastName = graphUser.getLastName();
			link = graphUser.getLink();
			middleName = graphUser.getMiddleName();
			name = graphUser.getName();
			username = graphUser.getUsername();
		} else {
			throw new IllegalArgumentException();
		}
	}

	private GraphUserParcelable(Parcel parcel) {
		birthday = parcel.readString();
		firstName = parcel.readString();
		id = parcel.readString();
		lastName = parcel.readString();
		link = parcel.readString();
		middleName = parcel.readString();
		name = parcel.readString();
		username = parcel.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public String getBirthday() {
		return birthday;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public String getLink() {
		return link;
	}

	// TODO if necessary:
	// public GraphLocation getLocation() {
	//     return null;
	// }

	public String getMiddleName() {
		return middleName;
	}

	public String getName() {
		return name;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(birthday);
		dest.writeString(firstName);
		dest.writeString(id);
		dest.writeString(lastName);
		dest.writeString(link);
		dest.writeString(middleName);
		dest.writeString(name);
		dest.writeString(username);
	}

}
