# Raytracer
This is a simple raytracer implementation in Java. It generates realistic images by simulating the interaction of light rays with 3D objects in a scene.

## Features
- Ray casting: Simulates the path of light rays from the camera to the objects in the scene.
- Shadows: Calculates shadows cast by objects based on the position of light sources.
- Reflections: Computes reflective surfaces on objects by tracing additional rays.
- Refractions: Simulates transparent materials with refractive indices.
- Diffuse and specular lighting: Calculates lighting effects on objects based on material properties.
- Constructive Solid Geometry (CSG): Supports combining objects using union, intersection, and difference operations.
- Skydome: Renders a skydome using an image to provide a realistic background.
## Usage
* Make sure you have Java installed on your system.
* Clone the repository: https://github.com/jala67/RayTracing.git
* Compile the Java files: javac RayTracing.java
* Run the raytracer: java RayTracing
* The rendered image will be displayed in a new window.
## Scene Configuration
To create your own scenes or modify existing ones, you can edit the main method in the RayTracer class. You can define objects, lights, camera settings, and material properties in the scene configuration section.

## Adding Objects
To add objects to the scene, use the addObject method and pass an instance of the desired object class. The supported object types include spheres, cubes, planes, tori, and CSG objects.

## Adding Lights
To add lights to the scene, use the addLight method and pass an instance of the Light class. Specify the position and intensity of the light source.

## Modifying Camera Settings
You can adjust the camera settings by modifying the Camera object's properties. Set the image width and height to define the resolution. Adjust the camera position and direction to control the viewpoint of the scene.

## Credits
This raytracer implementation is based on the principles of computer graphics and raytracing algorithms. It was developed as a learning project by Adrian and Jana.
