//
//  SecondViewController.swift
//  DeCom
//
//  Created by alireza azimi on 2020-01-18.
//  Copyright © 2020 alireza azimi. All rights reserved.
//
import UIKit
import CoreML
import Vision


class SecondViewController: UIViewController {
    
    // stores the label object on second view
    @IBOutlet weak var imageLabel: UILabel!
    // image view object on second view
    @IBOutlet weak var viewImage: UIImageView!
    // stores the image for the image view
    var thisImage: UIImage!
    // stores the image label from first view
    var thisIs: String!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // stores image accordingly
        viewImage.image = thisImage
        
        // change label text accordingly
        imageLabel.text = thisIs
        

    }
    

}
