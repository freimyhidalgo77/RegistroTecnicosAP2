import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Agenda de Tecnicos",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF2196F3)
                )
            )
        },

        ) { padding ->

                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clickable { navController.navigate("tecnicos") }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text("Técnicos", fontSize = 18.sp)
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clickable { navController.navigate("tickets") }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text("Tickets", fontSize = 18.sp)
                        }
                    }
                }
            }
        }


