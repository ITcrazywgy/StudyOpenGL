cmake_minimum_required(VERSION 3.4.1)

add_library(model-loader
            SHARED
            src/main/cpp/loader.cpp
            )

add_library( assimp SHARED IMPORTED )
set_target_properties( assimp PROPERTIES IMPORTED_LOCATION    ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libassimp.so )

find_library( log-lib log )

set(base_inc_dir src/main/cpp/include )
include_directories( BEFORE ${base_inc_dir} )

target_link_libraries(model-loader assimp ${log-lib})