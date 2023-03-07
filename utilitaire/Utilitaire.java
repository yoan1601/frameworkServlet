/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilitaire;

import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * @author yoan
 */
public class Utilitaire {
    public static String getURLPattern(HttpServletRequest request) throws Exception {
        String rep = request.getPathInfo();
        if(rep == null){
            rep = "/";
            return rep;
        }
        // raha asorina le / eo am voalohany
        return rep.substring(1);
    }
}
