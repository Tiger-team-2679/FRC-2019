cmake_minimum_required(VERSION 3.5)
project(vision)

find_package( OpenCV REQUIRED )
set (CMAKE_CXX_STANDARD 14)
set(WITH_CUDA ON)
include_directories( ${OpenCV_INCLUDE_DIRS} )

file(GLOB vision_SRC
        "src/main/cpp/*.hpp"
        "src/main/cpp/*.cpp"
        )

add_executable(vision ${vision_SRC})

target_link_libraries( vision opencv_core opencv_highgui opencv_videoio)