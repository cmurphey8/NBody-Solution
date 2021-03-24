/*******************************************************************************
 * 
 *  Dependencies: StdDraw.java, StdAudio.java
 *                Body.java, Star.java, BlackHole.java, Neutron.java, Pulsar.java
 *
 *  N-body simulation.
 *    *  Reads in input .txt file from the command line
 *    *  Reads in number of bodies N, radius of universe R, initial positions,
 *       velocities, masses, and name of image files from standard input;
 *    *  Simulate the system from time t = 0 until t >= T and plots the
 *       the results to standard drawing;
 *    *  Prints the final positions and velocities to standard output.
 *
 ******************************************************************************/

import java.util.Scanner;
import java.io.File; 
import java.io.FileNotFoundException;

public class NBody {

    // animation pause (in miliseconds)
    public static final int DELAY = 20;

    // music (2001 theme)
    public static final String MUSIC = "2001theme.wav";

    // background image
    public static final String BACKGROUND = "starfield.jpg";

    // parameters input file
    public static String PLANETS_FILE;

    // numerical constants
    public static final double G = 6.67e-11;    // gravitational constant (N m^2 / kg^2)
    public static final double T= 157788000.0;  // simulate from time 0 to T (s);             
    public static final double dt = 25000.0;    // time quantum (s);

    // parameters from first two lines 
    public static int N;                // number of bodies
    public static double R;             // radius of universe

    // global array of Body objects
    public static Body[] object;        // all Body objects in the simulation

    // read the planet file, new the parallel arrays, 
    // and load their values from the file.
    public static void loadFile() {
        
        // open a parameters File to read from
        Scanner scan = null;
        try { File f = new File(getFile()); scan = new Scanner( f ); } 
        catch(FileNotFoundException e) { System.out.println("File not found exception"); } 

        // read from standard input
        N = scan.nextInt();         // number of bodies
        R = scan.nextDouble();      // radius of universe (m)

        // declare Body type array size
        object = new Body[N];

        // read in initial position, velocity, mass, and image name from stdin
        for (int i = 0; i < N; i++) {
            object[i] = new Body(scan, R);      // assumes all other objects begin as stars
        }
    }

    public static String getFile() {
        Scanner console = new Scanner( System.in );
        System.out.print("parameters input file: ");
        String inputFile = "chaos.txt"; // console.next();
        console.close();
        return inputFile;
    }

    public static void runSimulation() {

        // run numerical simulation from 0 to T
        for (double t = 0.0; t < T; t += dt) {

            StdDraw.picture(0.0, 0.0, BACKGROUND);
            for (int i = 0; i < N; i++) {
                
                // calculate forces on each object
                object[i].zeroF();
                for (int j = 0; j < N; j++) { object[i].updateF(object[j], G); }

                // update velocities and positions
                object[i].step(dt, R);
                object[i].draw();  
            }
        }
        StdDraw.show();
        StdDraw.pause(DELAY);
    }

    public static void main(String[] args) {
        loadFile();

        // rescale coordinates that we can use natural x- and y-coordinates
        StdDraw.setXscale(-R, +R);
        StdDraw.setYscale(-R, +R);

        StdAudio.play( MUSIC );

        StdDraw.enableDoubleBuffering();

        // Run simulation
        runSimulation();

        // print final state of universe to standard output
        System.out.printf("%d\n", N);
        System.out.printf("%.2e\n", R);
        for (int i = 0; i < N; i++) {
            object[i].status();
        }

    }
}