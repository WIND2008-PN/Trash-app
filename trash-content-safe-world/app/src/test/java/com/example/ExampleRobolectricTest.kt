package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.model.WasteCategory
import com.example.data.repository.TrashSafeRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Trash Content Safe World", appName)
  }

  @Test
  fun `test tokenomics points calculation formula`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = AppDatabase.getInstance(context)
    val repo = TrashSafeRepository(db)

    // Formula: Points = (Volume / 100 * Weight) + Consistency - Fraud
    // 500ml PET (weight 1.5): volumeUnits = 5.0, raw = 7.5
    // streak 5 days: consistency = 5 * 2.5 = 12.5
    // fraud = 0.0 -> total = 7.5 + 12.5 = 20 points
    val points = repo.calculatePoints(
      volumeMl = 500,
      recyclabilityWeight = WasteCategory.PET_PLASTIC.recyclabilityWeight,
      streakDays = 5,
      fraudRiskScore = 0.0f
    )
    assertEquals(20, points)
    assertTrue(points > 0)
  }

  @Test
  fun `verify camera permission declaration in package manager`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val pm = context.packageManager
    val packageInfo = pm.getPackageInfo(context.packageName, android.content.pm.PackageManager.GET_PERMISSIONS)
    val permissions = packageInfo.requestedPermissions ?: emptyArray()
    assertTrue(permissions.contains(android.Manifest.permission.CAMERA))
  }
}

