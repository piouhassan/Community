package com.pollen.community.UrlConfig;

public class AppConfig {


	private static String URL = "http://192.168.1.9";

	// Server user login url
	public static String URL_LOGIN = URL+"/api/login.php";

	// Server user register url
	public static String URL_REGISTER = URL+"/api/register.php";

	// Server All Restaurant list url
	public static String URL_RESTAURANT = URL+ "/api/restaurant.php";

	public static String URL_ORDER = URL+ "/api/orders.php";

	public static String URL_GET_ORDER = URL+ "/api/get_orders.php?telephone=";

	public static String URL_CATEGORY = URL+ "/api/category.php?id=";

	public static final String URL_PROFIL = URL+"/api/profil.php";

	// get comment for  restaurant
	public static String URL_COMMENT_RESTAURANT_SEND = URL+ "/api/comment_restaurant_receive.php";

	public static String URL_COUNT_COMMENT = URL+ "/api/countcomment.php?id=";

	// set comment for  restaurant
	public static String URL_COMMENT_RESTAURANT = URL+ "/api/comment_restaurant.php?id=";

	public static String URL_PRODUCTS= URL+ "/api/products.php?id=";


	public static String URL_SEARCH = URL+ "/api/search.php";



	public static String URL_PICTURE= URL+"/api/";
}
