package com.boomer;
// used to temporarily hardcode images
public enum CityImageEnum {

	NEW_YORK("New York",R.drawable.new_york),
	LOS_ANGELES("Los Angeles",R.drawable.new_york2);
	
	private String city;
	private int img;
	
	
	private CityImageEnum(String city, int img) {
		this.city = city;
		this.img = img;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public int getImg() {
		return img;
	}


	public void setImg(int img) {
		this.img = img;
	}
	
	public static int getImageFromCity(String city){
		CityImageEnum[] cityEn = CityImageEnum.values();
		for (CityImageEnum rtype : cityEn) {
			if(rtype.city.equals(city)){
				return rtype.img;
			}
		}
		return 0;
	}
	
}
