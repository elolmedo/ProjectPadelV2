package es.romsolutions.padeltournament

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import es.romsolutions.padeltournament.data.database.AppDatabase
import es.romsolutions.padeltournament.data.model.League
import es.romsolutions.padeltournament.data.model.Tournament
import es.romsolutions.padeltournament.data.repository.LeagueRepository
import es.romsolutions.padeltournament.data.repository.PlayerRepository
import es.romsolutions.padeltournament.data.repository.TournamentRepository
import es.romsolutions.padeltournament.ui.components.*
import es.romsolutions.padeltournament.ui.screens.*
import es.romsolutions.padeltournament.ui.theme.ProjectPadelTheme
import es.romsolutions.padeltournament.ui.viewmodel.*
import es.romsolutions.padeltournament.auth.AuthManager
import es.romsolutions.padeltournament.billing.BillingManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    private val playerRepository by lazy { PlayerRepository(database.playerDao()) }
    private val tournamentRepository by lazy { TournamentRepository(database.tournamentDao(), database.matchDao()) }
    private val leagueRepository by lazy { LeagueRepository(database.leagueDao(), database.rankingDao(), database.matchDao(), database.teamDao()) }
    
    private val playerViewModel: PlayerViewModel by viewModels { PlayerViewModelFactory(playerRepository) }
    private val leagueViewModel: LeagueViewModel by viewModels { LeagueViewModelFactory(leagueRepository) }
    private val tournamentViewModel: TournamentViewModel by viewModels { TournamentViewModelFactory(tournamentRepository, leagueRepository, playerRepository) }

    private lateinit var authManager: AuthManager
    private lateinit var billingManager: BillingManager

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        
        authManager = AuthManager(this)
        billingManager = BillingManager(this)

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            
            // Estado para observar si el usuario está logueado
            var isLoggedIn by remember { mutableStateOf(authManager.isUserLoggedIn()) }
            val isProUser by billingManager.isPro.collectAsState()
            
            LaunchedEffect(Unit) {
                // Solo pedimos login si NO hay un usuario ya autenticado
                if (!authManager.isUserLoggedIn()) {
                    kotlinx.coroutines.delay(1000)
                    val success = authManager.signInWithGoogle()
                    if (success) {
                        isLoggedIn = true
                    }
                }
                
                // Una vez que sabemos el estado del login, actualizamos los ViewModels
                val currentAdminId = authManager.getCurrentUserId()
                playerViewModel.setAdminId(currentAdminId)
                leagueViewModel.setAdminId(currentAdminId)
                tournamentViewModel.setAdminId(currentAdminId)
            }

            ProjectPadelTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    var currentScreen by remember { mutableStateOf("Main") }
                    var selectedLeagueId by remember { mutableStateOf<Int?>(null) }
                    var selectedTournamentId by remember { mutableStateOf<Int?>(null) }
                    
                    BackHandler(enabled = currentScreen != "Main") { currentScreen = "Main" }

                    when (currentScreen) {
                        "Main" -> {
                            MainScreen(
                                playerViewModel, leagueViewModel, tournamentViewModel,
                                authManager = authManager,
                                isPro = isProUser,
                                windowWidthSizeClass = windowSizeClass.widthSizeClass,
                                onTogglePro = { /* Ya no existe el truco */ },
                                onNavigateToMatches = { leagueId ->
                                    selectedLeagueId = leagueId
                                    selectedTournamentId = null
                                    currentScreen = "Matches" 
                                },
                                onNavigateToTournamentMatches = { tournamentId ->
                                    selectedTournamentId = tournamentId
                                    selectedLeagueId = null
                                    currentScreen = "Matches"
                                },
                                onNavigateToProfile = {
                                    currentScreen = "Profile"
                                }
                            )
                        }
                        "Matches" -> {
                            MatchesScreen(
                                leagueViewModel = leagueViewModel,
                                tournamentViewModel = tournamentViewModel,
                                playerViewModel = playerViewModel,
                                initialLeagueId = selectedLeagueId,
                                initialTournamentId = selectedTournamentId,
                                onBack = { currentScreen = "Main" }
                            )
                        }
                        "Profile" -> {
                            ProfileScreen(
                                authManager = authManager,
                                billingManager = billingManager,
                                onBack = { currentScreen = "Main" }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    playerViewModel: PlayerViewModel, 
    leagueViewModel: LeagueViewModel,
    tournamentViewModel: TournamentViewModel,
    authManager: AuthManager,
    isPro: Boolean,
    windowWidthSizeClass: WindowWidthSizeClass,
    onTogglePro: () -> Unit,
    onNavigateToMatches: (Int?) -> Unit,
    onNavigateToTournamentMatches: (Int?) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val isExpanded = windowWidthSizeClass == WindowWidthSizeClass.Expanded

    val tabs = listOf(
        Pair(stringResource(R.string.tab_tournaments), Icons.Default.MilitaryTech),
        Pair(stringResource(R.string.tab_leagues), Icons.Default.EmojiEvents),
        Pair(stringResource(R.string.tab_players), Icons.Default.Person),
        Pair(stringResource(R.string.tab_ranking), Icons.Default.FormatListNumbered)
    )
    
    val currentTabTitle = tabs[selectedTabIndex].first
    
    var showAddPlayerDialog by remember { mutableStateOf(false) }
    var showAddLeagueDialog by remember { mutableStateOf(false) }
    var showAddTournamentDialog by remember { mutableStateOf(false) }
    var showProDialog by remember { mutableStateOf(false) }
    
    var preselectedPlayerIds by remember { mutableStateOf<List<Int>>(emptyList()) }

    val players by playerViewModel.allPlayers.collectAsState()
    val leagues by leagueViewModel.allLeagues.collectAsState()
    val tournaments by tournamentViewModel.allTournaments.collectAsState()

    if (showProDialog) {
        AlertDialog(
            onDismissRequest = { showProDialog = false },
            title = { Text(stringResource(R.string.pro_version)) },
            text = { Text(stringResource(R.string.pro_limit_reached)) },
            confirmButton = { Button(onClick = { onTogglePro(); showProDialog = false }) { Text(stringResource(R.string.subscribe)) } },
            dismissButton = { TextButton(onClick = { showProDialog = false }) { Text(stringResource(R.string.later)) } }
        )
    }

    Row(modifier = Modifier.fillMaxSize()) {
        if (isExpanded) {
            NavigationRail(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                header = {
                    val photoUrl = authManager.getCurrentUserPhotoUrl()
                    IconButton(onClick = onNavigateToProfile) {
                        if (photoUrl != null) {
                            AsyncImage(
                                model = photoUrl,
                                contentDescription = "Perfil",
                                modifier = Modifier.size(32.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(Icons.Default.Person, contentDescription = "Perfil")
                        }
                    }
                }
            ) {
                tabs.forEachIndexed { index, tab ->
                    NavigationRailItem(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        icon = { Icon(tab.second, contentDescription = tab.first) },
                        label = { Text(tab.first) }
                    )
                }
                
                Spacer(Modifier.weight(1f))
                
                if (selectedTabIndex <= 2) {
                    FloatingActionButton(
                        onClick = {
                            when (selectedTabIndex) {
                                0 -> if (!isPro && (leagues.size + tournaments.size >= 2)) showProDialog else showAddTournamentDialog = true
                                1 -> if (!isPro && (leagues.size + tournaments.size >= 2)) showProDialog else showAddLeagueDialog = true
                                2 -> if (!isPro && players.size >= 10) showProDialog else showAddPlayerDialog = true
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) { Icon(Icons.Default.Add, null) }
                    Spacer(Modifier.height(16.dp))
                }
            }
        }

        Scaffold(
            topBar = {
                if (!isExpanded) {
                    CenterAlignedTopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = stringResource(R.string.app_name),
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.clickable { onTogglePro() }
                                )
                                if (isPro) {
                                    Spacer(Modifier.width(8.dp))
                                    Surface(color = MaterialTheme.colorScheme.tertiary, shape = MaterialTheme.shapes.small) {
                                        Text("PRO", modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onTertiary, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        },
                        actions = {
                            val photoUrl = authManager.getCurrentUserPhotoUrl()
                            IconButton(onClick = onNavigateToProfile) {
                                if (photoUrl != null) {
                                    AsyncImage(
                                        model = photoUrl,
                                        contentDescription = "Perfil",
                                        modifier = Modifier.size(32.dp).clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(Icons.Default.Person, contentDescription = "Perfil")
                                }
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    )
                }
            },
            bottomBar = {
                if (!isExpanded) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        tonalElevation = 8.dp
                    ) {
                        tabs.forEachIndexed { index, tab ->
                            NavigationBarItem(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                icon = { Icon(tab.second, contentDescription = tab.first) },
                                label = { Text(tab.first, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                if (!isExpanded) {
                    if (selectedTabIndex <= 2) {
                        FloatingActionButton(
                            onClick = {
                                when (selectedTabIndex) {
                                    0 -> if (!isPro && (leagues.size + tournaments.size >= 2)) showProDialog = true else showAddTournamentDialog = true
                                    1 -> if (!isPro && (leagues.size + tournaments.size >= 2)) showProDialog = true else showAddLeagueDialog = true
                                    2 -> if (!isPro && players.size >= 10) showProDialog = true else showAddPlayerDialog = true
                                }
                            },
                            containerColor = MaterialTheme.colorScheme.primary
                        ) { Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add)) }
                    }
                }
            }
        ) { paddingValues ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                if (isExpanded) {
                    Text(
                        text = tabs[selectedTabIndex].first,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    when (selectedTabIndex) {
                        0 -> TournamentsListScreen(tournamentViewModel, playerViewModel, onTournamentStarted = onNavigateToTournamentMatches)
                        1 -> LeaguesListScreen(leagueViewModel, playerViewModel, onLeagueStarted = onNavigateToMatches)
                        2 -> PlayersListScreen(
                            viewModel = playerViewModel,
                            onNavigateToCreateLeague = { ids -> if (!isPro && (leagues.size + tournaments.size >= 2)) showProDialog = true else { preselectedPlayerIds = ids; showAddLeagueDialog = true } },
                            onNavigateToCreateTournament = { ids -> if (!isPro && (leagues.size + tournaments.size >= 2)) showProDialog = true else { preselectedPlayerIds = ids; showAddTournamentDialog = true } }
                        )
                        3 -> RankingListScreen(leagueViewModel, tournamentViewModel, playerViewModel)
                    }
                }
            }
        }
    }

    if (showAddPlayerDialog) {
        AddPlayerDialog(authManager = authManager, onDismiss = { showAddPlayerDialog = false }, onSave = { playerViewModel.insert(it); showAddPlayerDialog = false })
    }
    if (showAddLeagueDialog) {
        AddLeagueDialog(playerViewModel = playerViewModel, authManager = authManager, initialPlayerIds = preselectedPlayerIds, onDismiss = { showAddLeagueDialog = false; preselectedPlayerIds = emptyList() }, onSave = { league, ids -> leagueViewModel.insertLeagueWithPlayers(league, ids); showAddLeagueDialog = false; preselectedPlayerIds = emptyList() })
    }
    if (showAddTournamentDialog) {
        AddTournamentDialog(playerViewModel = playerViewModel, authManager = authManager, initialPlayerIds = preselectedPlayerIds, onDismiss = { showAddTournamentDialog = false; preselectedPlayerIds = emptyList() }, onSave = { tournament, ids -> tournamentViewModel.insertTournamentWithPlayers(tournament, ids); showAddTournamentDialog = false; preselectedPlayerIds = emptyList() })
    }
}
