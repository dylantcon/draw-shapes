# DrawShapes - A Responsive Java Drawing Application

## Project Overview

DrawShapes is a feature-rich Java drawing application developed in a single 6-hour sprint. The application provides a comprehensive set of drawing tools, allowing users to create various shapes including freehand sketches, rectangles, ellipses, and polygons with customizable stroke widths and colors.

## Key Features

- **Multiple Drawing Modes**: Supports dot-based freehand sketching, rectangle, ellipse, and polygon creation
- **Color Selection**: Full color palette with intelligent icon contrast adjustment based on selected color luminance
- **Adjustable Stroke Width**: Dynamic slider for precise control of line thickness
- **Action History**: Robust undo functionality and canvas clearing
- **Persistence**: Save and load functionality to preserve drawings between sessions
- **Interactive Preview**: Real-time preview of shapes during creation with semi-transparent rendering

## Technical Implementation

The application is built with a clean architecture that separates concerns across three primary classes:

1. **DrawShapes**: Main class handling application initialization, window management, and menu setup
2. **CanvasPanel**: Core rendering and interaction logic, managing the state of drawn objects and user input
3. **ToolPanel**: UI controls for drawing mode selection, color picking, and other operations

## Technical Highlights

### Event-Driven Architecture
The application leverages Java's event model through the implementation of MouseListener and MouseMotionListener interfaces, enabling responsive user interactions with the canvas.

### Object-Oriented Design
- **Encapsulation**: Each class maintains clear boundaries of responsibility
- **Composition**: The DrawNode class elegantly encapsulates drawing properties (color, position, type)
- **Polymorphism**: The PointType enumeration drives behavior differentiation across shape types

### Advanced Graphics Techniques
- Dynamic rendering of shapes with preview capabilities
- Implementation of custom drawing algorithms for different geometric primitives
- Intelligent point parsing for coordinate-based shape creation

### Memory Management
The application employs efficient data structures for tracking drawing elements:
- ArrayList for storing placed coordinates
- Stack for maintaining action sequence for undo operations

### Smart UI Components
- Custom button implementation with background color rendering
- Dynamic label sizing based on container dimensions
- Intelligent color luminance calculation for icon contrast adjustment

## Notable Algorithmic Insights

- **Polygon Snapping Algorithm**: Implements a proximity detection system that allows users to close polygons intuitively by snapping to the origin point when within a defined distance
- **Box Delimitation**: Abstract handling of rectangle and ellipse shapes through unified coordinate parsing logic
- **Action Sequence Tracking**: Sophisticated tracking of drawing actions that enables precise undo operations even for complex multi-point shapes

## Conclusion

DrawShapes demonstrates advanced Java programming skills and a deep understanding of UI design principles, event handling, and graphics programming. The clean architecture and thoughtful abstractions showcase software engineering expertise, particularly impressive given the rapid development timeframe of just six hours.
