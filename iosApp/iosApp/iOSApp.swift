import SwiftUI
import shared

@main
struct iOSApp: App {
    
    //Sorry for newbie code on iOS
    //There are lots of way to improve the SwiftUI implementation
    //I am a native android developer with almost 3 years experience
    //learning...learning...learning...
    
    
    //creating databaseDriverFactory
    //creating useCase
    let getAllAgentsUseCase = GetAllAgentsUseCase(databaseDriverFactory: DatabaseDriverFactory())
    
    var body: some Scene {
        WindowGroup {
            
            //we are menually injecting usecase
            //this can be done with koin(Dependency Injection framwork)
            ContentView(viewModel: .init(getAllAgentsUseCase: getAllAgentsUseCase))
        }
    }
}
