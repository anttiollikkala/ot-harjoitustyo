/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.util.Scanner;

/**
 *
 * @author anttiollikkala
 */
public class UI {
    private Scanner scanner;
    
    public UI(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public void Start() {
        while(true) {
            String cmd = this.scanner.nextLine();
            System.out.println(cmd);
        }
    }
    
}
