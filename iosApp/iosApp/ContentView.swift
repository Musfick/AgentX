import SwiftUI
import Kingfisher
import shared
import Combine

struct ContentView: View {
    
    @ObservedObject private(set) var viewModel: ViewModel
    
    @State private var index:Int = 0
    @State private var fullPortrait:String?
    @State private var background:String?
    
    
    var body: some View {
        
        var state = viewModel.state
        
        NavigationView {
            
            ZStack{
                Rectangle()
                    .fill(
                        LinearGradient(
                            gradient: Gradient(
                                colors: [Color(hex: 0xFFfb4353), Color(hex: 0xFF0f1822)]),
                            startPoint: .top,
                            endPoint: .bottom)
                    )
                    .ignoresSafeArea()
                
                //Show loading view everytime
                if(state.isLoading){
                    loadingView()
                }
                
                //Show error view if error happend and list is empty
                if(state.isError && state.agents.count == 0){
                    errorView(message: state.message)
                }
                
                //Show success view if error happen but list to not empty and if success
                if(state.isLoading == false && state.agents.count != 0){
                    successView(agents: state.agents)
                }
            }
        }
    }
    
    private func loadingView() -> AnyView {
        return AnyView(
            ProgressView() {
                Text("Loading...").foregroundColor(.white)
            }
            .progressViewStyle(CircularProgressViewStyle(tint: Color.white))
        )
    }
    
    private func errorView(message:String?) -> AnyView {
        return AnyView(
            VStack{
                Text(message ?? "Error occured").multilineTextAlignment(.center).foregroundColor(.white)
                Button("Retry") {
                    viewModel.getAllAgents()
                }
            }
        )
    }
    
    private func successView(agents: [Agent]) -> AnyView {
        
        return AnyView(
            VStack(spacing: 0) {
                
                //Show Diplay name top
                Text("\(agents[index].displayName!)")
                    .font(.custom("Valorant-Regular", size: 32)).foregroundColor(.white)
                
                
                //Agent View
                GeometryReader {
                    let size = $0.size
                    ZStack{
                        
                        //Show agent background behind the portrait
                        KFImage(URL(string: background ?? agents[0].background!)!)
                            .resizable()
                            .renderingMode(.template)
                            .foregroundColor(Color(hex: 0xFFffffff, alpha: 0.3))
                            .scaledToFit()
                            .frame(width: size.width, height: size.height/1.6)
                            .clipped()
                            .onChange(of: index) { newValue in
                                withAnimation(.easeInOut(duration: 0.3)){
                                    self.background = agents[newValue].background
                                }
                            }
                        
                        //Show Agent portrait top of agent background
                        KFImage(URL(string: fullPortrait ?? agents[0].fullPortrait!)!)
                            .resizable()
                            .scaledToFill()
                            .frame(width: size.width, height: size.height)
                            .clipped()
                            .onChange(of: index) { newValue in
                                withAnimation(.easeInOut(duration: 0.3)){
                                    self.fullPortrait = agents[newValue].fullPortrait
                                }
                            }
                    }
                }
                
     
                //Agent Selection View
                Spacer(minLength: 18)
                GeometryReader{
                    let size = $0.size
                    let pageWidth:CGFloat = size.width/3
                    let imageWidth:CGFloat = 80
                    
                    ScrollView(.horizontal, showsIndicators: false){
                        HStack(spacing: 0){
                            ForEach(agents, id: \.self){ agent in
                                ZStack{
                                    KFImage(URL(string: agent.displayIcon!)!)
                                        .resizable()
                                        .aspectRatio(contentMode: .fill)
                                        .frame(width: imageWidth, height: size.height)
                                        .background(Color(hex: 0xFF183040))
                                }.frame(width: pageWidth, height: size.height)
                            }
                        }
                        .padding(.horizontal, (size.width - pageWidth) / 2)
                        .background(SnapCarouselHelper(pageWidth: pageWidth, pageCount: agents.count, index: $index))
                    }
                    .overlay(RoundedRectangle(cornerRadius: 0, style: .continuous)
                        .stroke(Color(hex: 0xFFfb4353), lineWidth: 3.5)
                        .frame(width: imageWidth, height: size.height)
                        .allowsHitTesting(false))
                }
                .frame(height: 80)
            }
        )
    }
    
}


extension ContentView {
    
    
    @MainActor
    class ViewModel: ObservableObject {
        
        let getAllAgentsUseCase: GetAllAgentsUseCase
        
        @Published var state = MainUiState()
        
        init(getAllAgentsUseCase: GetAllAgentsUseCase) {
            self.getAllAgentsUseCase = getAllAgentsUseCase
            self.getAllAgents()
        }
        
        func getAllAgents() {
            
            getAllAgentsUseCase.allAgents.watch { state in
                
                //State manage need to improve
                if(state is ResourceLoading<ListWrapper<Agent>>){
                    
                    let data = (state as! ResourceLoading<ListWrapper<Agent>>).data
                    
                    if data?.items != nil {
                        
                        let items = data?.items as! [Agent]
                        
                        if(items.count != 0){
                            //loading state but list is not empty thats why we can show the agent instead of loading screen
                            self.state = MainUiState(isLoading: false, isError: false, agents: items, message: nil)
                        }else{
                            //showing loading state becasue list is empty
                            self.state = MainUiState(isLoading: true, isError: false, agents: [], message: nil)
                        }
                        
                    }else{
                        //if any upper case is not match
                        self.state = MainUiState(isLoading: true, isError: false, agents: [], message: nil)
                    }
                    
                }
                
                if(state is ResourceSuccess<ListWrapper<Agent>>){
                    let data = (state as! ResourceSuccess<ListWrapper<Agent>>).data
                    
                    //show agent list if state is success
                    var items = data?.items as! [Agent]
                    self.state = MainUiState(isLoading: false, isError: false, agents: items, message: nil)
                }
                
                if(state is ResourceError<ListWrapper<Agent>>){
                    var data = (state as! ResourceError<ListWrapper<Agent>>).data
                    var message = (state as! ResourceError<ListWrapper<Agent>>).message
                    
                    if data?.items != nil {
                        var items = data?.items as! [Agent]
                        if(items.count == 0){
                            //If error occur and agent list it empty
                            //show error
                            self.state = MainUiState(isLoading: false, isError: true, agents: items, message: message)
                        }else{
                            //if error happen but agent list is not empty because it read from local cache
                            //instead of showing error show the list which already read from locacl cache
                            self.state = MainUiState(isLoading: false, isError: false, agents: items, message: message)
                        }
                    }else{
                        //if any of up case not match show the error
                        self.state = MainUiState(isLoading: false, isError: true, agents: [] , message: message)
                    }
                }
                
            }
        }
    }}

//require
extension Agent: Identifiable { }


extension Color {
    init(hex: UInt, alpha: Double = 1) {
        self.init(
            .sRGB,
            red: Double((hex >> 16) & 0xff) / 255,
            green: Double((hex >> 08) & 0xff) / 255,
            blue: Double((hex >> 00) & 0xff) / 255,
            opacity: alpha
        )
    }
}
