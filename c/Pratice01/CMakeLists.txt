cmake_minimum_required(VERSION 3.8)
project(Pratice01)

set(CMAKE_CXX_STANDARD 11)

set(ROOT_INCLUDE_DIR D:/DevLib/dev/include)
set(ROOT_LIB_DIR D:/DevLib/dev/lib)

include_directories(${ROOT_INCLUDE_DIR})
link_directories(${ROOT_LIB_DIR}/GLFW)

set(SOURCE_FILES main.cpp glad.c)
add_executable(Pratice01 ${SOURCE_FILES})


target_link_libraries(Pratice01 glfw3 opengl32)
