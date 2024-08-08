package com.HotelApp.common.constants;

public class ValidationConstants {

    public static final String HOTEL_INFO_NOT_FOUND = "Hotel Info not found";

    public static final String NAME_BLANK = "Please, provide a name.";

    public static final String NAME_LENGTH_TOO_SHORT = "The name should contain at least 2 characters.";

    public static final String NAME_LENGTH_TOO_LONG = "The name is too long.";

    public static final String EMAIL_NOT_BLANK = "Email should be provided.";

    public static final String EMAIL_TOO_LONG = "Your email is too long.";

    public static final String INVALID_EMAIL = "Please enter real email...";

    public static final String INVALID_AGE = "We need your age to verify that you are over 18 years old.";

    public static final String AGE_IS_REQUIRED = "Age is required.";

    public static final String INVALID_AGE_OVER_100 = "Maximum value for age exceeded, needs to be under 100.";

    public static final String NEGATIVE_AGE = "Age cannot be negative.";

    public static final String MINIMUM_AGE = "Age needs to be over 18 years!";

    public static final String EMPTY_PASSWORD = "You need password for your registration...";

    public static final String PASSWORD_LENGTH = "Use strong password needs to be at least 5 characters!";

    public static final String CONFIRM_PASSWORD = "Please confirm your password.";

    public static final String OLD_PASSWORD_MISMATCH = "Old password mismatch.";

    public static final String PASSWORD_MISMATCH = "Password mismatch";

    public static final String EMAIL_EXIST = "Email is occupied.";

    public static final String MESSAGE_BLANK = "Leave a message here...";

    public static final String MESSAGE_TOO_SHORT = "You need to say at least Hi...";

    public static final String MESSAGE_TOO_LONG = "Your comment is too long.";

    public static final String TEXT_TOO_LONG = "Additional info is too long.";

    public static final String DOCUMENT_ID_EMPTY = "We need the document id of the guest.";

    public static final String DOCUMENT_ID_TOO_LONG = "Document id of the guest is too long.";

    public static final String ROOM_NUMBER_REQUIRED = "Room number is required!";

    public static final String NEGATIVE_DAYS = "No negative days!";

    public static final String EMPTY_DAYS = "You should enter the days that guest want to stay.";

    public static final String PHONE_NUMBER_TOO_LONG = "Your phone number is too long.";

    public static final String SMTP_MESSAGE_VOUCHER = "Please connect the smtp server to proceed sending a bonus voucher to client.";

    public static final String SMTP_MESSAGE_RESERVATION = "Please connect the smtp server to proceed sending confirmation email.";

    public static final String FILE_NOT_ALLOWED = "Something went wrong. Please choose different file.";

    public static final String FORBIDDEN_USER = "You can't see this user :)";

    public static final String USER_NOT_FOUND = "User not found for email: ";

    public static final long IMAGE_MAX_SIZE = 5 * 1024 * 1024;

    public static final String EXTENSION_NOT_ALLOWED = "File type not supported.";

    public static final String EMPTY_FILE = "Please select a file.";
}
