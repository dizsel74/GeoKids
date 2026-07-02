package com.example.ui.components

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun GeometryWebView(
    shape: String,
    showVertices: Boolean,
    showEdges: Boolean,
    showFaces: Boolean,
    modifier: Modifier = Modifier
) {
    val htmlContent = remember { getHtmlContent() }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                }
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        // Inject initial states once loaded
                        view?.evaluateJavascript(
                            "setShape('$shape', $showVertices, $showEdges, $showFaces)",
                            null
                        )
                    }
                }
                loadDataWithBaseURL("file:///android_asset/", htmlContent, "text/html", "UTF-8", null)
            }
        },
        update = { webView ->
            webView.evaluateJavascript(
                "setShape('$shape', $showVertices, $showEdges, $showFaces)",
                null
            )
        },
        modifier = modifier
    )
}

private fun getHtmlContent(): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
            <style>
                body {
                    margin: 0;
                    padding: 0;
                    overflow: hidden;
                    background-color: #F8F9FA;
                    user-select: none;
                    -webkit-user-select: none;
                    touch-action: none;
                }
                #canvas-container {
                    width: 100vw;
                    height: 100vh;
                    display: block;
                    position: relative;
                }
                canvas {
                    display: block;
                    width: 100%;
                    height: 100%;
                }
                #loading {
                    position: absolute;
                    top: 10px;
                    left: 10px;
                    font-family: sans-serif;
                    font-size: 12px;
                    color: #888;
                    pointer-events: none;
                }
            </style>
            <!-- Try to load Three.js from CDN for premium WebGL rendering -->
            <script src="https://cdnjs.cloudflare.com/ajax/libs/three.js/r128/three.min.js" onerror="onThreeError()"></script>
        </head>
        <body>
            <div id="canvas-container">
                <div id="loading">Loading 3D...</div>
                <canvas id="geometryCanvas"></canvas>
            </div>

            <script>
                var hasThree = false;
                var currentShape = 'cube';
                var config = {
                    showVertices: true,
                    showEdges: true,
                    showFaces: true
                };

                // Check if Three.js loaded successfully
                try {
                    if (typeof THREE !== 'undefined') {
                        hasThree = true;
                        document.getElementById('loading').innerText = "3D WebGL (Three.js)";
                    } else {
                        throw new Error("Three.js not loaded");
                    }
                } catch(e) {
                    onThreeError();
                }

                function onThreeError() {
                    hasThree = false;
                    document.getElementById('loading').innerText = "3D Interactive Canvas";
                }

                // Global setShape function called from Android
                window.setShape = function(shape, vertices, edges, faces) {
                    currentShape = shape;
                    config.showVertices = vertices;
                    config.showEdges = edges;
                    config.showFaces = faces;
                    
                    if (hasThree && threeRenderer) {
                        updateThreeShape();
                    } else if (!hasThree && canvasRenderer) {
                        canvasRenderer.setShape(shape);
                    }
                };

                // Touch and drag rotation states
                var rotationX = 0.5;
                var rotationY = 0.6;
                var isDragging = false;
                var previousMousePosition = { x: 0, y: 0 };

                var container = document.getElementById('canvas-container');
                
                function handleStart(clientX, clientY) {
                    isDragging = true;
                    previousMousePosition = { x: clientX, y: clientY };
                }

                function handleMove(clientX, clientY) {
                    if (!isDragging) return;
                    var deltaX = clientX - previousMousePosition.x;
                    var deltaY = clientY - previousMousePosition.y;

                    rotationY += deltaX * 0.01;
                    rotationX += deltaY * 0.01;

                    // Limit vertical rotation to avoid flipping upside down
                    rotationX = Math.max(-Math.PI/3, Math.min(Math.PI/3, rotationX));

                    previousMousePosition = { x: clientX, y: clientY };
                }

                function handleEnd() {
                    isDragging = false;
                }

                container.addEventListener('mousedown', function(e) { handleStart(e.clientX, e.clientY); });
                container.addEventListener('mousemove', function(e) { handleMove(e.clientX, e.clientY); });
                window.addEventListener('mouseup', handleEnd);

                container.addEventListener('touchstart', function(e) {
                    if (e.touches.length > 0) handleStart(e.touches[0].clientX, e.touches[0].clientY);
                });
                container.addEventListener('touchmove', function(e) {
                    if (e.touches.length > 0) handleMove(e.touches[0].clientX, e.touches[0].clientY);
                });
                window.addEventListener('touchend', handleEnd);


                // ==========================================
                // THREE.JS RENDERER IMPLEMENTATION
                // ==========================================
                var threeRenderer = null;
                var scene, camera, renderer, currentMesh, verticesGroup, edgesGroup;
                var pointLight, ambientLight;

                if (hasThree) {
                    initThree();
                }

                function initThree() {
                    var canvas = document.getElementById('geometryCanvas');
                    scene = new THREE.Scene();
                    scene.background = new THREE.Color(0xF8F9FA);

                    camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 0.1, 100);
                    camera.position.z = 5;

                    renderer = new THREE.WebGLRenderer({ canvas: canvas, antialias: true });
                    renderer.setSize(window.innerWidth, window.innerHeight);
                    renderer.setPixelRatio(window.devicePixelRatio);

                    // Add lights
                    ambientLight = new THREE.AmbientLight(0xffffff, 0.6);
                    scene.add(ambientLight);

                    pointLight = new THREE.PointLight(0xffffff, 0.8);
                    pointLight.position.set(5, 5, 5);
                    scene.add(pointLight);

                    // Create containers
                    verticesGroup = new THREE.Group();
                    edgesGroup = new THREE.Group();
                    scene.add(verticesGroup);
                    scene.add(edgesGroup);

                    threeRenderer = {
                        animate: function() {
                            requestAnimationFrame(threeRenderer.animate);
                            
                            // Auto rotation when not dragging
                            if (!isDragging) {
                                rotationY += 0.005;
                            }

                            if (currentMesh) {
                                currentMesh.rotation.x = rotationX;
                                currentMesh.rotation.y = rotationY;
                            }
                            verticesGroup.rotation.x = rotationX;
                            verticesGroup.rotation.y = rotationY;
                            edgesGroup.rotation.x = rotationX;
                            edgesGroup.rotation.y = rotationY;

                            // Update visibility based on config
                            if (currentMesh) currentMesh.visible = config.showFaces;
                            verticesGroup.visible = config.showVertices;
                            edgesGroup.visible = config.showEdges;

                            renderer.render(scene, camera);
                        }
                    };

                    updateThreeShape();
                    threeRenderer.animate();

                    window.addEventListener('resize', function() {
                        camera.aspect = window.innerWidth / window.innerHeight;
                        camera.updateProjectionMatrix();
                        renderer.setSize(window.innerWidth, window.innerHeight);
                    });
                }

                function updateThreeShape() {
                    if (!hasThree) return;

                    // Clean previous elements
                    if (currentMesh) scene.remove(currentMesh);
                    while(verticesGroup.children.length > 0) verticesGroup.remove(verticesGroup.children[0]);
                    while(edgesGroup.children.length > 0) edgesGroup.remove(edgesGroup.children[0]);

                    var geometry, meshMaterial, edgesGeometry, lineMaterial;
                    var vertexPositions = [];

                    // Material colors: Soft playful baby blue for faces, bold dark blue for edges, orange for vertices
                    meshMaterial = new THREE.MeshPhongMaterial({
                        color: 0x4E7DFF,
                        transparent: true,
                        opacity: 0.75,
                        side: THREE.DoubleSide,
                        flatShading: true,
                        shininess: 80
                    });

                    lineMaterial = new THREE.LineBasicMaterial({
                        color: 0x2244CC,
                        linewidth: 4 // Note: linewidth > 1 usually not supported by WebGL, so we draw thick edges
                    });

                    if (currentShape === 'cube') {
                        geometry = new THREE.BoxGeometry(1.2, 1.2, 1.2);
                        currentMesh = new THREE.Mesh(geometry, meshMaterial);
                        scene.add(currentMesh);

                        // Vertex list
                        vertexPositions = [
                            new THREE.Vector3(-0.6, -0.6, -0.6), new THREE.Vector3(0.6, -0.6, -0.6),
                            new THREE.Vector3(0.6, 0.6, -0.6), new THREE.Vector3(-0.6, 0.6, -0.6),
                            new THREE.Vector3(-0.6, -0.6, 0.6), new THREE.Vector3(0.6, -0.6, 0.6),
                            new THREE.Vector3(0.6, 0.6, 0.6), new THREE.Vector3(-0.6, 0.6, 0.6)
                        ];

                        // Edges
                        var edgesGeo = new THREE.EdgesGeometry(geometry);
                        var lineSegments = new THREE.LineSegments(edgesGeo, new THREE.LineBasicMaterial({ color: 0x1A35B3, linewidth: 3 }));
                        edgesGroup.add(lineSegments);

                    } else if (currentShape === 'prism') {
                        geometry = new THREE.BoxGeometry(1.8, 0.9, 0.9);
                        currentMesh = new THREE.Mesh(geometry, meshMaterial);
                        scene.add(currentMesh);

                        vertexPositions = [
                            new THREE.Vector3(-0.9, -0.45, -0.45), new THREE.Vector3(0.9, -0.45, -0.45),
                            new THREE.Vector3(0.9, 0.45, -0.45), new THREE.Vector3(-0.9, 0.45, -0.45),
                            new THREE.Vector3(-0.9, -0.45, 0.45), new THREE.Vector3(0.9, -0.45, 0.45),
                            new THREE.Vector3(0.9, 0.45, 0.45), new THREE.Vector3(-0.9, 0.45, 0.45)
                        ];

                        var edgesGeo = new THREE.EdgesGeometry(geometry);
                        var lineSegments = new THREE.LineSegments(edgesGeo, new THREE.LineBasicMaterial({ color: 0x1A35B3, linewidth: 3 }));
                        edgesGroup.add(lineSegments);

                    } else if (currentShape === 'pyramid') {
                        // Let's create a custom pyramid with square base
                        geometry = new THREE.BufferGeometry();
                        
                        // Coordinates: 5 vertices
                        var v = [
                            -0.6, -0.5, -0.6,  // 0: Back Left
                             0.6, -0.5, -0.6,  // 1: Back Right
                             0.6, -0.5,  0.6,  // 2: Front Right
                            -0.6, -0.5,  0.6,  // 3: Front Left
                             0.0,  0.7,  0.0   // 4: Apex
                        ];

                        vertexPositions = [
                            new THREE.Vector3(v[0], v[1], v[2]),
                            new THREE.Vector3(v[3], v[4], v[5]),
                            new THREE.Vector3(v[6], v[7], v[8]),
                            new THREE.Vector3(v[9], v[10], v[11]),
                            new THREE.Vector3(v[12], v[13], v[14])
                        ];

                        // Faces indices (triangles only for BufferGeometry)
                        var indices = [
                            // Base (two triangles)
                            3, 1, 0,   3, 2, 1,
                            // Sides
                            0, 1, 4,
                            1, 2, 4,
                            2, 3, 4,
                            3, 0, 4
                        ];

                        var vertices = new Float32Array([
                            // base triangle 1
                            v[9], v[10], v[11],  v[3], v[4], v[5],  v[0], v[1], v[2],
                            // base triangle 2
                            v[9], v[10], v[11],  v[6], v[7], v[8],  v[3], v[4], v[5],
                            // back side
                            v[0], v[1], v[2],    v[3], v[4], v[5],  v[12], v[13], v[14],
                            // right side
                            v[3], v[4], v[5],    v[6], v[7], v[8],  v[12], v[13], v[14],
                            // front side
                            v[6], v[7], v[8],    v[9], v[10], v[11], v[12], v[13], v[14],
                            // left side
                            v[9], v[10], v[11],  v[0], v[1], v[2],  v[12], v[13], v[14]
                        ]);

                        geometry.setAttribute('position', new THREE.BufferAttribute(vertices, 3));
                        geometry.computeVertexNormals();

                        currentMesh = new THREE.Mesh(geometry, meshMaterial);
                        scene.add(currentMesh);

                        // Edges of pyramid
                        var edgesIndices = [
                            0,1, 1,2, 2,3, 3,0, // Base
                            0,4, 1,4, 2,4, 3,4  // Lateral
                        ];
                        
                        var linePositions = [];
                        for (var i = 0; i < edgesIndices.length; i++) {
                            var idx = edgesIndices[i];
                            linePositions.push(v[idx*3], v[idx*3+1], v[idx*3+2]);
                        }
                        
                        var edgesGeo = new THREE.BufferGeometry();
                        edgesGeo.setAttribute('position', new THREE.Float32BufferAttribute(linePositions, 3));
                        var lineSegments = new THREE.LineSegments(edgesGeo, new THREE.LineBasicMaterial({ color: 0x1A35B3, linewidth: 3 }));
                        edgesGroup.add(lineSegments);
                    }

                    // Add sphere helpers for Vertices
                    var sphereGeo = new THREE.SphereGeometry(0.08, 16, 16);
                    var sphereMat = new THREE.MeshPhongMaterial({
                        color: 0xFF5A36, // Coral red/orange
                        emissive: 0x220000,
                        flatShading: false
                    });

                    for (var i = 0; i < vertexPositions.length; i++) {
                        var sphere = new THREE.Mesh(sphereGeo, sphereMat);
                        sphere.position.copy(vertexPositions[i]);
                        verticesGroup.add(sphere);
                    }
                }


                // ==========================================
                // OFFLINE HTML5 CANVAS 3D RENDERER (FALLBACK)
                // ==========================================
                var canvasRenderer = null;

                if (!hasThree) {
                    initCanvasRenderer();
                }

                function initCanvasRenderer() {
                    var canvas = document.getElementById('geometryCanvas');
                    var ctx = canvas.getContext('2d');

                    function resizeCanvas() {
                        canvas.width = window.innerWidth;
                        canvas.height = window.innerHeight;
                    }
                    resizeCanvas();
                    window.addEventListener('resize', resizeCanvas);

                    // 3D coordinates models
                    var models = {
                        cube: {
                            vertices: [
                                {x: -0.6, y: -0.6, z: -0.6}, // 0
                                {x:  0.6, y: -0.6, z: -0.6}, // 1
                                {x:  0.6, y:  0.6, z: -0.6}, // 2
                                {x: -0.6, y:  0.6, z: -0.6}, // 3
                                {x: -0.6, y: -0.6, z:  0.6}, // 4
                                {x:  0.6, y: -0.6, z:  0.6}, // 5
                                {x:  0.6, y:  0.6, z:  0.6}, // 6
                                {x: -0.6, y:  0.6, z:  0.6}  // 7
                            ],
                            edges: [
                                [0,1], [1,2], [2,3], [3,0], // back
                                [4,5], [5,6], [6,7], [7,4], // front
                                [0,4], [1,5], [2,6], [3,7]  // connectors
                            ],
                            faces: [
                                {indices: [0, 1, 2, 3], normal: {x: 0, y: 0, z: -1}}, // back
                                {indices: [4, 5, 6, 7], normal: {x: 0, y: 0, z: 1}},  // front
                                {indices: [0, 1, 5, 4], normal: {x: 0, y: -1, z: 0}}, // bottom
                                {indices: [2, 3, 7, 6], normal: {x: 0, y: 1, z: 0}},  // top
                                {indices: [0, 3, 7, 4], normal: {x: -1, y: 0, z: 0}}, // left
                                {indices: [1, 2, 6, 5], normal: {x: 1, y: 0, z: 0}}   // right
                            ]
                        },
                        prism: {
                            vertices: [
                                {x: -0.9, y: -0.45, z: -0.45}, // 0
                                {x:  0.9, y: -0.45, z: -0.45}, // 1
                                {x:  0.9, y:  0.45, z: -0.45}, // 2
                                {x: -0.9, y:  0.45, z: -0.45}, // 3
                                {x: -0.9, y: -0.45, z:  0.45}, // 4
                                {x:  0.9, y: -0.45, z:  0.45}, // 5
                                {x:  0.9, y:  0.45, z:  0.45}, // 6
                                {x: -0.9, y:  0.45, z:  0.45}  // 7
                            ],
                            edges: [
                                [0,1], [1,2], [2,3], [3,0],
                                [4,5], [5,6], [6,7], [7,4],
                                [0,4], [1,5], [2,6], [3,7]
                            ],
                            faces: [
                                {indices: [0, 1, 2, 3], normal: {x: 0, y: 0, z: -1}},
                                {indices: [4, 5, 6, 7], normal: {x: 0, y: 0, z: 1}},
                                {indices: [0, 1, 5, 4], normal: {x: 0, y: -1, z: 0}},
                                {indices: [2, 3, 7, 6], normal: {x: 0, y: 1, z: 0}},
                                {indices: [0, 3, 7, 4], normal: {x: -1, y: 0, z: 0}},
                                {indices: [1, 2, 6, 5], normal: {x: 1, y: 0, z: 0}}
                            ]
                        },
                        pyramid: {
                            vertices: [
                                {x: -0.6, y: -0.5, z: -0.6}, // 0
                                {x:  0.6, y: -0.5, z: -0.6}, // 1
                                {x:  0.6, y: -0.5, z:  0.6}, // 2
                                {x: -0.6, y: -0.5, z:  0.6}, // 3
                                {x:  0.0, y:  0.7, z:  0.0}  // 4 apex
                            ],
                            edges: [
                                [0,1], [1,2], [2,3], [3,0], // base
                                [0,4], [1,4], [2,4], [3,4]  // sides
                            ],
                            faces: [
                                {indices: [3, 2, 1, 0], normal: {x: 0, y: -1, z: 0}}, // base
                                {indices: [0, 1, 4], normal: {x: 0, y: 0.5, z: -0.86}}, // back side
                                {indices: [1, 2, 4], normal: {x: 0.86, y: 0.5, z: 0}}, // right side
                                {indices: [2, 3, 4], normal: {x: 0, y: 0.5, z: 0.86}}, // front side
                                {indices: [3, 0, 4], normal: {x: -0.86, y: 0.5, z: 0}}  // left side
                            ]
                        }
                    };

                    // Function to rotate a 3D point around Y then X
                    function rotatePoint(point, rx, ry) {
                        // Rotate Y (horizontal)
                        var cosY = Math.cos(ry);
                        var sinY = Math.sin(ry);
                        var x1 = point.x * cosY - point.z * sinY;
                        var z1 = point.x * sinY + point.z * cosY;

                        // Rotate X (vertical)
                        var cosX = Math.cos(rx);
                        var sinX = Math.sin(rx);
                        var y2 = point.y * cosX - z1 * sinX;
                        var z2 = point.y * sinX + z1 * cosX;

                        return { x: x1, y: y2, z: z2 };
                    }

                    // Project 3D point to 2D Canvas screen space
                    function project(point, width, height) {
                        var scale = Math.min(width, height) * 0.45; // Perspective scale factor
                        var distance = 3.5; // distance of camera
                        // Perspective divide
                        var f = scale / (distance - point.z);
                        var u = width / 2 + point.x * f;
                        var v = height / 2 - point.y * f; // Invert Y axis
                        return { x: u, y: v, depth: point.z };
                    }

                    canvasRenderer = {
                        setShape: function(shape) {
                            // Automatically triggers redraw through cycle
                        }
                    };

                    function draw() {
                        ctx.clearRect(0, 0, canvas.width, canvas.height);

                        // Auto-rotate if not dragging
                        if (!isDragging) {
                            rotationY += 0.005;
                        }

                        var model = models[currentShape];
                        if (!model) return;

                        var w = canvas.width;
                        var h = canvas.height;

                        // Rotate and project all vertices
                        var transformedVertices = [];
                        for (var i = 0; i < model.vertices.length; i++) {
                            var rotated = rotatePoint(model.vertices[i], rotationX, rotationY);
                            var projected = project(rotated, w, h);
                            transformedVertices.push({
                                orig: model.vertices[i],
                                rot: rotated,
                                proj: projected
                            });
                        }

                        // Draw Faces (Depth sorted!)
                        if (config.showFaces) {
                            // Calculate average Z depth for each face to do Painter's Algorithm
                            var sortedFaces = [];
                            for (var i = 0; i < model.faces.length; i++) {
                                var face = model.faces[i];
                                var sumZ = 0;
                                for (var j = 0; j < face.indices.length; j++) {
                                    sumZ += transformedVertices[face.indices[j]].rot.z;
                                }
                                var avgZ = sumZ / face.indices.length;
                                sortedFaces.push({
                                    face: face,
                                    avgZ: avgZ
                                });
                            }

                            // Sort: back faces (smaller Z) first, front faces (larger Z) last
                            sortedFaces.sort(function(a, b) {
                                return a.avgZ - b.avgZ;
                            });

                            // Draw each face with lighting shading
                            for (var i = 0; i < sortedFaces.length; i++) {
                                var face = sortedFaces[i].face;
                                var idxs = face.indices;

                                // Rotate the face normal vector
                                var rotatedNormal = rotatePoint(face.normal, rotationX, rotationY);
                                // Light vector (coming from top-right-front: 0.5, 0.7, 1.0 normalized)
                                var light = { x: 0.5, y: 0.7, z: 0.8 };
                                var len = Math.sqrt(light.x*light.x + light.y*light.y + light.z*light.z);
                                light.x /= len; light.y /= len; light.z /= len;

                                // Dot product between rotated normal and light direction
                                var dot = rotatedNormal.x * light.x + rotatedNormal.y * light.y + rotatedNormal.z * light.z;
                                // Map dot to shading scale (0.4 to 1.0)
                                var intensity = 0.4 + 0.6 * Math.max(0, dot);

                                // Base face color: Playful blue (#4E7DFF)
                                // Convert to HSL/RGB for shading
                                var r = Math.floor(78 * intensity);
                                var g = Math.floor(125 * intensity);
                                var b = Math.floor(255 * intensity);

                                ctx.fillStyle = 'rgba(' + r + ',' + g + ',' + b + ', 0.8)';
                                ctx.strokeStyle = 'rgba(' + r + ',' + g + ',' + b + ', 0.3)';
                                ctx.lineWidth = 1;

                                ctx.beginPath();
                                ctx.moveTo(transformedVertices[idxs[0]].proj.x, transformedVertices[idxs[0]].proj.y);
                                for (var j = 1; j < idxs.length; j++) {
                                    ctx.lineTo(transformedVertices[idxs[j]].proj.x, transformedVertices[idxs[j]].proj.y);
                                }
                                ctx.closePath();
                                ctx.fill();
                                ctx.stroke();
                            }
                        }

                        // Draw Edges (Bold dark lines)
                        if (config.showEdges) {
                            ctx.strokeStyle = '#2244CC';
                            ctx.lineWidth = 5;
                            ctx.lineCap = 'round';
                            ctx.lineJoin = 'round';

                            for (var i = 0; i < model.edges.length; i++) {
                                var e = model.edges[i];
                                var p1 = transformedVertices[e[0]].proj;
                                var p2 = transformedVertices[e[1]].proj;
                                
                                ctx.beginPath();
                                ctx.moveTo(p1.x, p1.y);
                                ctx.lineTo(p2.x, p2.y);
                                ctx.stroke();
                            }
                        }

                        // Draw Vertices (Highlight corners with bright orange/red spheres)
                        if (config.showVertices) {
                            // Depth-sort vertices so back vertices look smaller or are drawn behind
                            var sortedVertices = transformedVertices.slice();
                            sortedVertices.sort(function(a, b) {
                                return a.rot.z - b.rot.z;
                            });

                            for (var i = 0; i < sortedVertices.length; i++) {
                                var v = sortedVertices[i].proj;
                                
                                // Dynamic radius based on camera depth perspective
                                var radius = 10 * (1.5 + v.depth);
                                radius = Math.max(4, Math.min(18, radius));

                                // Draw circle border
                                ctx.beginPath();
                                ctx.arc(v.x, v.y, radius, 0, 2 * Math.PI);
                                ctx.fillStyle = '#FF5A36';
                                ctx.strokeStyle = '#FFFFFF';
                                ctx.lineWidth = 3;
                                ctx.fill();
                                ctx.stroke();

                                // Draw a small specular highlight circle inside for 3D sphere look!
                                ctx.beginPath();
                                ctx.arc(v.x - radius * 0.3, v.y - radius * 0.3, radius * 0.25, 0, 2 * Math.PI);
                                ctx.fillStyle = '#FFFFFF';
                                ctx.fill();
                            }
                        }

                        requestAnimationFrame(draw);
                    }

                    draw();
                }
            </script>
        </body>
        </html>
    """.trimIndent()
}
