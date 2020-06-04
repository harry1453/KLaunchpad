cmake_minimum_required(VERSION 3.16)
project(KLaunchpadExamples)
set(CMAKE_CXX_STANDARD 20)

#add_library(KLaunchpad SHARED KLaunchpad.lib)
#set_target_properties(KLaunchpad PROPERTIES LINKER_LANGUAGE C)

add_executable(KLaunchpadExamples main.cpp)
#target_link_libraries(KLaunchpadExamples KLaunchpad)
link_directories(C:\\Users\\Harry\\Documents\\Development\\Other\\kotlin-launchpad\\examples\\c++)
target_link_libraries(KLaunchpadExamples KLaunchpad.lib)