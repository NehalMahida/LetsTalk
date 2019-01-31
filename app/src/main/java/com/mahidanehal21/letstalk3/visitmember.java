package com.mahidanehal21.letstalk3;

import android.content.Intent;

public class visitmember {
    private String name;
    private String number;
    private String id;
    private String image;
    private String status;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {

        return status;
    }

    public visitmember(String name, String number, String id) {
        this.name = name;
        this.number = number;
        this.id = id;
    }

    public visitmember() {

    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {

        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {

        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getId() {
        return id;
    }
    public  int hashCode(){
        int k = Integer.parseInt(""+(number.charAt(9)+number.charAt(7)+number.charAt(8)+number.charAt(6)));
        return k;
    }
    public boolean equals(Object o){
        if(o instanceof visitmember){
            visitmember ob = (visitmember)o;
            if(ob.number.equals(number))
                return true;
                else
                    return false;
            }
            return false;

    }
}
