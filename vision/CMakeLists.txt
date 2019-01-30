cmake_minimum_required(VERSION 3.12)
project(vision)

set(CMAKE_CXX_STANDARD 14)

find_package( OpenCV REQUIRED )
add_executable(vision src/main/cpp/main.cpp)
target_link_libraries( vision ${OPENCV_LIBS} )