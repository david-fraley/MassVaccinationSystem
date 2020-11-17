<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!doctype html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="x-ua-compatible" content="IE=edge">
        
        <title>Mirth Connect Administrator</title>
        
        <link rel="shortcut icon" type="image/x-icon" href="images/favicon.ico" />
        <link rel="stylesheet" type="text/css" href="css/bootstrap.css" />
        <link rel="stylesheet" type="text/css" href="css/main.css" />
        
        <script type="text/javascript">
            /* Break out of frame if inside a frame. */
            if (window != window.top) {
                window.top.location = window.location;
            }
        </script>
        
        <script type="text/javascript" src="js/jquery-1.12.4.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    
    <body id="body" style="display: none;" class="subpage" <c:if test="${actionBean.secureHttps == true}">onload="document.loginform.username.focus();"</c:if>>
        <div id="centerWrapper" class="container">
            <div class="row">
                <div id="mirthLogoWrapper">
                    <img id="mirthLogo" src="images/mirthconnectlogowide.png" />
                </div>
    
                <div id="mcadministrator" class="col-xs-6 col-xs-6-custom">
                    <h1 style="text-align: center;">Mirth Connect Administrator</h1>
                    
                    <div id="launchbuttoncontainer">
	                    <div class="help-block">
	                        Click the big green button below, and choose to open the file with the Administrator Launcher instead of using Java Web Start. If you don't have the Administrator Launcher installed, click the big blue button below.
	                    </div>
                        <a class="btn btn-md btn-themebutton" href="javascript:launchAdministrator()">Launch Mirth Connect Administrator</a>
                        <div id="optionsDropdownContainer" class="dropdown" style="text-align: center;">
                        	<button id="optionsButton" class="btn btn-default btn-lg dropdown-toggle opt-button" data-toggle="dropdown" role="button">
	                        	<span class="glyphicon glyphicon-cog"></span>
                        	</button>
                        	<ul id="optionsDropdownMenu" class="dropdown-menu" role="menu" aria-labelledby="optionsButton">
                        		<li role="presentation"><span class="text-center">Web Start Settings</span></li>
                        		<li class="divider"></li>
                        		<li role="presentation">
                        			<span id="maxHeapSizeLabel" class="dropdown-label pull-left">Max Heap Size:&nbsp;</span>
                        			<select id="maxHeapSizeSelect" class="dropdown-select"></select>
                        			<p id="maxHeapSizeWarning" class="dropdown-warning"><b>Note:</b> The Administrator may fail to start if the max heap size is set too high.</p>
                        		</li>
                        	</ul>
                        </div>
	                </div>
                    
                    <div>
	                    <div class="help-block">
	                        <strong>Mirth Connect Administrator Launcher:</strong><br /> This is a separate application that replaces<br/>Java Web Start and allows you to launch the Administrator from your local workstation.  
	                    </div>
                        <a class="btn btn-md btn-downloadbutton" href="javascript:downloadAdministratorLauncher()">Download Administrator Launcher</a>
                        <div id="administratorLauncherOptionsDropdownContainer" class="dropdown">
                        	<button id="administratorLauncherOptionsButton" class="btn btn-default btn-lg dropdown-toggle opt-button" data-toggle="dropdown" role="button">
	                        	<span class="glyphicon glyphicon-cog"></span>
                        	</button>
                        	<ul id="administratorLauncherOptionsDropdownMenu" class="dropdown-menu" role="menu" aria-labelledby="administratorLauncherOptionsButton" style="text-align: center;">
                        		<li role="presentation"><span class="text-center">Operating System</span></li>
                        		<li class="divider"></li>
                        		<li role="presentation">
                        			<select id="operatingSystemSelect" class="dropdown-select"></select>
                        			<p id="operatingSystemWarning" class="dropdown-warning">Select the operating system you want to install the Administrator Launcher on.</p>
                        		</li>
                        	</ul>
                        </div>
                    </div>
                </div>
    
                <div id="webdashboardsignin" class="col-xs-6 col-xs-6-custom">
                    <h1 id="webDashboardHeader" style="text-align: center;">Web Dashboard Sign in</h1>
    				    				
    				<c:choose>
    				 	<c:when test="${actionBean.secureHttps == true}">
		                     <form id="webLoginForm" name="loginform" action="Login.action" method="post">
		                        <div id="loginErrorAlert" class="alert alert-danger hide fade in" data-alert="alert">
			                    	<p>Invalid login credentials</p>
			                    </div>
			                    <div id="webLoginWrapper">
			    					<div id="webLogin">
			                            <input type="hidden" name="op" value="login" /> <input type="hidden" name="version" value="0.0.0" /> <label for="username">Username</label>
			                            <input id="username" type="text" name="username" autocomplete="off" maxlength="32" /> <label for="password">Password</label>
			                            <input id="password" type="password" name="password" autocomplete="off" />
			                            <div id="webLoginSecurityReminder" class="help-block">
			                                <strong>Security Reminder:</strong><br /> Sign out of your account when you finish your session.
			                            </div>
				                    </div>
			                    </div>
			                    <div id="webLoginButton">
				                	<input class="btn btn-md btn-themebutton" type="submit" value="Sign in"/>
				                </div>
				        	</form>
		            	</c:when>
		              	<c:otherwise>
		              		<div id="securesiteaccess">
		              			<p id="httpsInfoParagraph">The Mirth Connect Web Dashboard must be accessed over HTTPS. Click below button to switch to the secure site.</p>
		                   		<div class="help-block">
		                	        <br/><strong>Note:</strong><br/> You may see a certificate error if your server is using a <a href="http://en.wikipedia.org/wiki/Self-signed_certificate" target="_blank">self-signed certificate</a>. To prevent further warnings, you can add this certificate to your browser or operating system.
		                        </div>
			                </div>
		                    <div id="accessSecureSiteButton" style="text-align: center;">
		                    	<a class="btn btn-md btn-themebutton" href="javascript:accessSecureSite()">Access Secure Site</a>
		                    </div> 
  						</c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <div id="smallSubPage">
        	<p>&copy; 2019 NextGen Healthcare | Mirth Connect</p>
        </div>
    
        <script type="text/javascript">
            $(document).ready(
                    function detectMobile() {
                        /**
                         * jQuery.browser.mobile (http://detectmobilebrowser.com/)
                         * jQuery.browser.mobile will be true if the browser is a mobile device
                         * Regex updated: 1 August 2014
                         **/
                        (function(a) {
							(jQuery.browser=jQuery.browser||{}).mobile=/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4))
						})(navigator.userAgent||navigator.vendor||window.opera);

                        if (jQuery.browser.mobile) {
                            $("#mcadministrator").hide();

                            $("#centerWrapper").css("margin", "0");
                            $("#centerWrapper").css("padding", "0");
                            $("#centerWrapper").css("border", "none");
                            $("#centerWrapper").css("width", "100%");

                            $("#webdashboardsignin").css("border-left", "0");
                            $("#webdashboardsignin").css("margin-left", "0");
                            $("#webdashboardsignin").css("padding-left", "0");
                            $("#webdashboardsignin").css("width", "100%");

                            $("#username").css("width", "100%");
                            $("#password").css("width", "100%");

                            $("#securesiteaccess").css("margin-left", "30px");
    
                            // Set viewport meta tag
                            var mt = $('meta[name=viewport]');
            				mt = mt.length ? mt : $('<meta name="viewport" />').appendTo('head');
            				mt.attr('content', 'initial-scale=.8,maximum-scale=.8,user-scalable=no,width=device-width');
                        } else {
                            $("#mcadministrator").show();
                        }
                        $("#body").css("display", "inline");
                    });
        </script>
        <script type="text/javascript">
            var showAlert = false;
            $(document).ready(function() {
            
            	/**** Administrator Max Heap Size Options ****/
            	
            	// Get the default max heap size and options from the context
                var defaultMaxHeapSize = convertHeapSizeString('${actionBean.context.maxHeapSize}', 512);
                var maxHeapSizeOptions = '${actionBean.context.maxHeapSizeOptions}';
                var options = [];
                
                // Set the options if they were specified in the context
                if (maxHeapSizeOptions) {
                	var optionsStringArray = maxHeapSizeOptions.split(',');
                	
                	for (var i = 0; i < optionsStringArray.length; i++) {
                		var heapSize = convertHeapSizeString(optionsStringArray[i]);
                		if (heapSize) {
                			options.push(heapSize);
                		}
                	}
                }
                
                // If no options were specified or they were invalid, set the defaults
                if (options.length == 0) {
	                options = [256,512,1024,2048];
                }
                
                // Determine if the default is contained in the options array
                var found = false;
                for (var i = 0; i < options.length; i++) {
                	if (options[i].toString() == defaultMaxHeapSize) {
                		found = true;
                	}
                }
                
                // If not, put it in the array
                if (!found) {
                	options.push(defaultMaxHeapSize);
                }
                
                // Sort the options array
                options.sort(function(a,b) {return a-b});
                
                // Build the options HTML for the select input
                var selectHtml = '';
                for (var i = 0; i < options.length; i++) {
                	var num = options[i];
                	var selected = (num.toString() == defaultMaxHeapSize);
                	var letter = 'm';
                	
                	if (num % 1024 == 0) {
                		num /= 1024;
                		letter = 'g';
                	}
                	selectHtml += '<option value="' + num + letter + '"' + (selected ? ' selected' : '') + '>' + num + ' ' + letter.toUpperCase() + 'B</option>';
                }
                
                // Set the options
                $('#maxHeapSizeSelect').html(selectHtml);
                
                // This prevents closing the Bootstrap dropdown when clicking on the select input 
                $('#optionsDropdownMenu').click(function(e) {
                	e.stopPropagation();
                });
                
                /**** Administrator Launcher Options ****/
                
                var platform = '';
            	if (window.navigator) {
            		if (window.navigator.oscpu) {
            			platform = window.navigator.oscpu;
            		} else if (window.navigator.platform) {
            			platform = window.navigator.platform;
            		}
            	}
            		
        		if (platform.toLowerCase().indexOf('mac') >= 0) {
        			platform = 'macos';
        		} else if (platform.toLowerCase().indexOf('win') >= 0) {
        			if ((!window.navigator.userAgent || window.navigator.userAgent.indexOf('64') < 0) && (platform.indexOf('32') >= 0 || (window.navigator.cpuClass && window.navigator.cpuClass.indexOf('86') >= 0))) {
        				platform = 'windows';
        			} else {
        				platform = 'windows-x64';
        			}
        		} else {
        			platform = 'linux';
        		}
        		
        		var operatingSystemSelectHtml = '';
        		operatingSystemSelectHtml += '<option value="macos.dmg"' + (platform == 'macos' ? ' selected' : '') + '>macOS</option>';
        		operatingSystemSelectHtml += '<option value="linux.sh"' + (platform == 'linux' ? ' selected' : '') + '>Linux</option>';
        		operatingSystemSelectHtml += '<option value="windows.exe"' + (platform == 'windows' ? ' selected' : '') + '>Windows 32-bit</option>';
        		operatingSystemSelectHtml += '<option value="windows-x64.exe"' + (platform == 'windows-x64' ? ' selected' : '') + '>Windows 64-bit</option>';
        		
        		// Set the options
        		$('#operatingSystemSelect').html(operatingSystemSelectHtml);
        		
        		// This prevents closing the Bootstrap dropdown when clicking on the select input 
                $('#administratorLauncherOptionsDropdownMenu').click(function(e) {
                	e.stopPropagation();
                });
                
                /**** Show Error Alert ****/
                
                $.urlParam = function(name) {
                    var results = new RegExp('[\\?&]' + name + '=([^&#]*)').exec(window.location.href);
                    if (results != null) {
                        return results[1] || 0;
                    }
                }
                showAlert = $.urlParam('showAlert');

                if (showAlert) {
                    $("#loginErrorAlert").removeClass('hide');
                    return false;
                } else {
                    $("#loginErrorAlert").addClass('hide');
                    return true;
                }
            });
            
            function convertHeapSizeString(str, defaultSize) {
            	str = new String(str);
            	defaultSize = new Number(defaultSize) || 0;
                var heapSize = new Number(str.replace(/[^\d]/g,'')) || defaultSize;
                
                var heapSizeLetter = str.replace(/[^mg]/ig,'');
                if (!/[mg]/i.test(heapSizeLetter)) {
                	heapSizeLetter = 'm';
                }
                if (heapSizeLetter.toLowerCase() == 'g') {
                	heapSize *= 1024;
                }
                
                return heapSize;
            }
        </script>
        <script type="text/javascript">
        	function downloadAdministratorLauncher(){
        		var url = '${actionBean.context.currentScheme}://' + window.location.hostname + ':${actionBean.context.currentPort}${actionBean.context.contextPath}/launcher/' + $('#operatingSystemSelect').val();
        		
        		$.ajax({url: url, type: 'HEAD', success: function() {
        			window.location.href = url;
        		}, error: function() {
        			var suffix = $('#operatingSystemSelect').val();
        			if (suffix == 'linux.sh') {
        				suffix = 'unix.sh';
        			}
        			window.location.href = 'https://s3.amazonaws.com/downloads.mirthcorp.com/connect-client-launcher/mirth-administrator-launcher-1.1.0-' + suffix;
        		}});
       		}
        
       		function launchAdministrator(){
       			window.location.href = '${actionBean.context.currentScheme}://' + window.location.hostname + ':${actionBean.context.currentPort}${actionBean.context.contextPath}/webstart.jnlp?time=' + new Date().getTime() + '&maxHeapSize=' + $('#maxHeapSizeSelect').val();
       		}
       		
       		function accessSecureSite(){
       			window.location.href = 'https://' + window.location.hostname + ':${actionBean.context.httpsPort}' + window.location.pathname;
       		}
        </script>
    </body>
</html>