<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<%@ page isELIgnored="false"%> 
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8" />
		<title>Build Metadata Service</title>
		<meta name="keywords" content="Build, Metadata" />
		<meta name="description" content="Manage Builds Binaries Info" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<%@ include file="../include/css.jsp"%>
	</head>

	<body class="skin-1">
		<!-- topbar starts -->
        <%@ include file="../include/top.jsp"%>
        <!-- topbar ends -->

		<div class="main-container" id="main-container">
			<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
			</script>

			<div class="main-container-inner">
				<a class="menu-toggler" id="menu-toggler" href="#">
					<span class="menu-text"></span>
				</a>

				<!-- topbar starts -->
                <%@ include file="../include/left.jsp"%>
                <!-- topbar ends -->

				<div class="main-content">
					<div class="breadcrumbs" id="breadcrumbs">
						<script type="text/javascript">
							try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
						</script>

						<ul class="breadcrumb">
							<li>
								<i class="icon-home home-icon"></i>
								<a href="#">Home</a>
							</li>
							<li class="active">Builds</li>
						</ul><!-- .breadcrumb -->

					</div>

					<div class="page-content">

						<div class="row">
							<div class="col-xs-12">
								<!-- PAGE CONTENT BEGINS -->
                                
                                <div class="row">
									<div class="col-xs-12">
										
										<div class="table-header">
											Component Builds
										</div>

										<div class="table-responsive">
                                            
											<table id="sample-table-2" class="table table-striped table-bordered table-hover">
                                                <thead>
													<tr>
														<th class="center">
															<label>
																<input type="checkbox" class="ace" />
																<span class="lbl"></span>
															</label>
														</th>
														<th>ArtifactId</th>
                                                        <th>Version</th>
														<th class="hidden-480">GitRepo</th>

														<th>
															Branch
														</th>
														<th class="hidden-480">Type</th>

														<th></th>
													</tr>
												</thead>
                                                
                                                <tbody>
                                                    <c:forEach var="build" items="${builds}">
                                                        <tr>
                                                            <td class="center">
                                                                <label>
                                                                    <input type="checkbox" class="ace" />
                                                                    <span class="lbl"></span>
                                                                </label>
                                                            </td>

                                                            <td>${build.artifactId}</td>

                                                            <td>
                                                                <a href="/ws/builds/data/id/${build.id}">${build.version}</a>
                                                            </td>

                                                            <td class="hidden-480">${build.repoUrl}</td>
                                                            <td>${build.branch}</td>

                                                            <td class="hidden-480">
                                                                <span class="label label-sm label-warning">Contiuous</span>
                                                            </td>
                                                            <td>
                                                                Actions
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>

								<!-- PAGE CONTENT ENDS -->
							</div><!-- /.col -->
						</div><!-- /.row -->
					</div><!-- /.page-content -->
				</div><!-- /.main-content -->
			</div><!-- /.main-container-inner -->
		</div><!-- /.main-container -->

		<!-- topbar starts -->
        <%@ include file="../include/js.jsp"%>
        <!-- topbar ends -->
            
        <script src="/assets/js/jquery.dataTables.min.js"></script>
		<script src="/assets/js/jquery.dataTables.bootstrap.js"></script>
            
        <script type="text/javascript">
			jQuery(function($) {
				var oTable1 = $('#sample-table-2').dataTable( {
				"aoColumns": [
			      { "bSortable": false },
			      null, null,null, null, null,
				  { "bSortable": false }
				] } );
				
				
				$('table th input:checkbox').on('click' , function(){
					var that = this;
					$(this).closest('table').find('tr > td:first-child input:checkbox')
					.each(function(){
						this.checked = that.checked;
						$(this).closest('tr').toggleClass('selected');
					});
						
				});
			
			
				$('[data-rel="tooltip"]').tooltip({placement: tooltip_placement});
				function tooltip_placement(context, source) {
					var $source = $(source);
					var $parent = $source.closest('table')
					var off1 = $parent.offset();
					var w1 = $parent.width();
			
					var off2 = $source.offset();
					var w2 = $source.width();
			
					if( parseInt(off2.left) < parseInt(off1.left) + parseInt(w1 / 2) ) return 'right';
					return 'left';
				}
			})
		</script>
</body>
</html>

