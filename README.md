# Draw Shapes Application

Welcome to **Draw Shapes**, an interactive Java-based application for artistic and geometric creativity! This program demonstrates the versatility of Java Swing and highlights polished code architecture to create a seamless user experience for drawing and saving shapes.

## Features

### Interactive Shape Drawing

- **Free-Drawing Mode**: Sketch fluidly by dragging your mouse across the canvas.

- **Rectangle Mode**: Drag your mouse to define a bounding box for rectangles.

- **Ellipse Mode**: Similar to rectangles, but creates smooth ellipses.

- **Polygon Mode**: Add vertices by clicking on the canvas, and close the shape by clicking near the starting point.

### Customization Options

- **Color Selection**: Use a color palette to personalize your shapes.

- **Stroke Width Control**: Adjust stroke thickness dynamically using an intuitive slider.

### State Management

- **Undo Functionality**: Revert your last action seamlessly.

- **Clear Canvas**: Reset your work with a single click.

- **Save and Load**: Save your creations as binary files and reload them anytime.

## Design Architecture

### Modularity and Reusability

The project employs a modular design for ease of scalability and maintainability:

- **CanvasPanel**: Handles rendering and interaction logic for the drawing surface.

- **ToolPanel**: Provides user controls for shape types, color selection, and other tools.

- **ColSP Class**: Encapsulates color, coordinates, and point type, ensuring efficient data management.

### Object-Oriented Principles

The application adheres to clean OOP principles:

- Encapsulation is leveraged through well-defined getters and setters.

- Specialized classes (e.g., `DrawSeqElement`) simplify complex behaviors such as undoing actions.

### Serialization for State Persistence

Using Java's `Serializable` interface, the application enables:

- Efficient storage of drawing states (colors, shapes, and sequences).

- Robust error handling during save/load operations.

### Advanced UI Design

The user interface prioritizes:

- **Dynamic Tool Interaction**: Buttons respond intuitively, adapting to luminance for enhanced visibility.

- **Preview Feedback**: Shapes provide translucent previews during creation.

- **Custom Rendering**: Leverages `Graphics2D` for anti-aliased and visually appealing shapes. Could easily be expanded to feature different stroke types.

## Why It Stands Out

- **Elegant Logic**: The `CanvasPanel` effectively handles complex geometries like polygons and previews with minimal redundancy.

- **User-Focused Design**: Fine-tuned interactions ensure a smooth and intuitive experience.

- **Scalability**: Adding new tools or features would require minimal refactoring thanks to its modular architecture.

## How to Run

- Download the attached `.jar` file, and the program should boot up if you have the JRE installed on your machine. I have a package structure in place, so if you want to pack and run it manually, you'll need to compile the code using `javac` and the `-d` flag, and run it using `java` and the `-cp` flag:

- `javac -d bin -sourcepath src src/drawshapes/DrawShapes.java`

- `java -cp bin drawshapes.DrawShapes`

## Technical Highlights

### Challenges Solved

1. **Efficient Shape Rendering**: Used `LinkedList`, `Stack`, and `ListIterator` to manage actions and provide undo functionality without performance bottlenecks.

2. **Custom Geometry Parsing**: Implemented utilities like `parsePoints` and `showGeom` to simplify coordinate management for shapes.

3. **Dynamic Previews**: Designed translucent rendering for in-progress shapes, enhancing usability.

### Technologies Used

- Java Swing for UI

- Java AWT for graphics rendering

- Serialization for state management

## Future Enhancements

- **Shape Editing**: Add functionality to modify existing shapes.

- **Export Options**: Save drawings as image files (e.g., PNG, JPG).

- **Multi-layer Support**: Allow multiple canvases with layering capabilities.

## Contributing

Feel free to fork this repository and contribute! If you encounter any issues or have suggestions, open an issue or submit a pull request.

## License

This project is licensed under the MIT License. See the LICENSE file for details.

Thank you for exploring Draw Shapes! I hope you enjoy creating beautiful and functional designs with this application.


