//
//  ViewController.swift
//  DeCom
//
//  Created by alireza azimi on 2020-01-18.
//  Copyright © 2020 alireza azimi. All rights reserved.
//

import UIKit
import CoreML
import Vision

class ViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    // object handles the image picker: camera or gallery?
    let imagePicker = UIImagePickerController()
    // a temporary storage for user interface image
    var storeImage: UIImage!
    // identification
    var itIs: String!
    
    // constructor of the class
    override func viewDidLoad() {
        super.viewDidLoad()
        // set image picker to camera. do not allow editing
        imagePicker.delegate = self
        imagePicker.sourceType = .camera
        imagePicker.allowsEditing = false
        
    }
    
    /**
     function handles extracting image from image picker
     function creates segueway
     */
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        // Obtaining user image as UIImage
        if let userPickedImage = info[UIImagePickerController.InfoKey.originalImage] as? UIImage {
            storeImage = userPickedImage
            
            
            guard let ciimage = CIImage(image: userPickedImage) else{
                fatalError("Could not convert to CI imge")
            }
            
            detect(image: ciimage)
            
        }
        //close the image picker window, go to segue way
        
        imagePicker.dismiss(animated: true){
            self.performSegue(withIdentifier: "goToResult", sender: self)
        }
        
    }
    
    /**
     Machine learning object detector
     */
    
    func detect(image: CIImage){
        guard let model = try? VNCoreMLModel(for: Inceptionv3().model) else{
            fatalError("Loading CoreML model Failed")
            
        }
        
        let request = VNCoreMLRequest(model: model){(request, error) in
            guard let results = request.results as? [VNClassificationObservation] else{
                fatalError("Model failed to process image")
            }
            // obtaining the most relevant result
            
            if let firstResult = results.first{
                var firstObject = (firstResult.identifier).split(separator: ",").map(String.init)[0]
                
                self.itIs = firstObject // itIs stores the detected object
                
                
            }
            
        }
        
        // request handler for obtaining image
        let handler = VNImageRequestHandler(ciImage: image)
        
        do{
            try! handler.perform([request])
        }
        catch {
            print(error)
        }
        
    }
    
    // preparing segueway for data transfer
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "goToResult" {
            let destinationVC = segue.destination as! SecondViewController
            if storeImage != nil {
                destinationVC.thisImage = storeImage
                destinationVC.thisIs = itIs
                
            }
        }
    }
    
    // checkes if camera button is pressed
    @IBAction func cameraPressed(_ sender: UIButton) {

        openCamera()
        
    }
    
    // handles opening camera
    func openCamera(){
        imagePicker.delegate = self
        imagePicker.sourceType = .camera
        imagePicker.allowsEditing = false
        self.present(imagePicker, animated: true, completion: nil)
        
    }
    
    // checks if open gallery button is pressed
    @IBAction func galleryPressed(_ sender: UIButton) {
        openGallary()
        
    }
    
    // handles opening gallery
    func openGallary()
    {
        imagePicker.allowsEditing = false
        imagePicker.sourceType = UIImagePickerController.SourceType.photoLibrary
        present(imagePicker, animated: true, completion: nil)
    }
    
}

