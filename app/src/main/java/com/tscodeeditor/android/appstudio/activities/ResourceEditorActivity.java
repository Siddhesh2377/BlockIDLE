/*
 * This file is part of Android AppStudio [https://github.com/TS-Code-Editor/AndroidAppStudio].
 *
 * License Agreement
 * This software is licensed under the terms and conditions outlined below. By accessing, copying, modifying, or using this software in any way, you agree to abide by these terms.
 *
 * 1. **  Copy and Modification Restrictions  **
 *    - You are not permitted to copy or modify the source code of this software without the permission of the owner, which may be granted publicly on GitHub Discussions or on Discord.
 *    - If permission is granted by the owner, you may copy the software under the terms specified in this license agreement.
 *    - You are not allowed to permit others to copy the source code that you were allowed to copy by the owner.
 *    - Modified or copied code must not be further copied.
 * 2. **  Contributor Attribution  **
 *    - You must attribute the contributors by creating a visible list within the application, showing who originally wrote the source code.
 *    - If you copy or modify this software under owner permission, you must provide links to the profiles of all contributors who contributed to this software.
 * 3. **  Modification Documentation  **
 *    - All modifications made to the software must be documented and listed.
 *    - the owner may incorporate the modifications made by you to enhance this software.
 * 4. **  Consistent Licensing  **
 *    - All copied or modified files must contain the same license text at the top of the files.
 * 5. **  Permission Reversal  **
 *    - If you are granted permission by the owner to copy this software, it can be revoked by the owner at any time. You will be notified at least one week in advance of any such reversal.
 *    - In case of Permission Reversal, if you fail to acknowledge the notification sent by us, it will not be our responsibility.
 * 6. **  License Updates  **
 *    - The license may be updated at any time. Users are required to accept and comply with any changes to the license.
 *    - In such circumstances, you will be given 7 days to ensure that your software complies with the updated license.
 *    - We will not notify you about license changes; you need to monitor the GitHub repository yourself (You can enable notifications or watch the repository to stay informed about such changes).
 * By using this software, you acknowledge and agree to the terms and conditions outlined in this license agreement. If you do not agree with these terms, you are not permitted to use, copy, modify, or distribute this software.
 *
 * Copyright © 2024 Dev Kumar
 */

package com.tscodeeditor.android.appstudio.activities;

import android.os.Bundle;
import android.view.View;
import com.tscodeeditor.android.appstudio.R;
import com.tscodeeditor.android.appstudio.databinding.ActivityResourceEditorBinding;
import com.tscodeeditor.android.appstudio.models.ProjectModel;
import com.tscodeeditor.android.appstudio.utils.EnvironmentUtils;
import com.tscodeeditor.android.appstudio.utils.serialization.DeserializerUtils;
import com.tscodeeditor.android.appstudio.utils.serialization.ProjectModelSerializationUtils;
import java.io.File;

public class ResourceEditorActivity extends BaseActivity {
  // SECTION Constants
  public static final int RESOURCES_SECTION = 0;
  public static final int NO_RESOURCE_SECTION = 1;
  public static final int LOADING_SECTION = 2;

  private ActivityResourceEditorBinding binding;

  /*
   * Contains the location of project directory.
   * For example: /../../Project/100
   */
  private File projectRootDirectory;

  /*
   * Contains the location of project directory.
   * For example: /../../Project/100/../src/main/res
   */
  private File resourceDirectory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityResourceEditorBinding.inflate(getLayoutInflater());

    setContentView(binding.getRoot());

    binding.toolbar.setTitle(R.string.app_name);
    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);

    projectRootDirectory = new File(getIntent().getStringExtra("projectRootDirectory"));

    switchSection(LOADING_SECTION);

    ProjectModelSerializationUtils.deserialize(
        new File(projectRootDirectory, EnvironmentUtils.PROJECT_CONFIGRATION),
        new ProjectModelSerializationUtils.DeserializerListener() {

          @Override
          public void onSuccessfullyDeserialized(ProjectModel object) {
            if (getIntent().hasExtra("resourceDir")) {
              resourceDirectory = new File(getIntent().getStringExtra("resourceDir"));
              switchSection(RESOURCES_SECTION);
            } else {
              switchSection(NO_RESOURCE_SECTION);
            }
          }

          @Override
          public void onFailed(int errorCode, Exception e) {
            switchSection(NO_RESOURCE_SECTION);
          }
        });
  }

  public void switchSection(int section) {
    binding.resourceView.setVisibility(section == RESOURCES_SECTION ? View.VISIBLE : View.GONE);
    binding.noResourceSection.setVisibility(
        section == NO_RESOURCE_SECTION ? View.VISIBLE : View.GONE);
    binding.loading.setVisibility(section == LOADING_SECTION ? View.VISIBLE : View.GONE);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    binding = null;
  }
}
