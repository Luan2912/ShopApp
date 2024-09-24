package com.project.shopapp.exceptions;

//Kiểm tra và phân loại exception
public class DataNotFoundException extends Exception{
    public DataNotFoundException(String message){
        super(message);
    }
}
